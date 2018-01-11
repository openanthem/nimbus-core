/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;

import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.antheminc.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.core.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.core.domain.model.state.StateEventDelegator;
import com.antheminc.oss.nimbus.core.domain.model.state.StateEventListener;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class DefaultStateEventDelegator implements StateEventDelegator {
	
	private List<StateEventListener> defaultScopedListeners;
	
	private static final ThreadLocal<List<StateEventListener>> listenersInThread = new ThreadLocal<List<StateEventListener>>() {
		@Override
	 	protected List<StateEventListener> initialValue() {
			return new ArrayList<>();
		}
	};

	
	@Override
	public void addTxnScopedListener(StateEventListener listener) {
		listenersInThread.get().add(listener);
	}
	
	@Override
	public boolean removeTxnScopedListener(StateEventListener listener) {
		return listenersInThread.get().remove(listener);
	}
	
	@Override
	public void onStartRuntime(ExecutionRuntime execRt) {
		delegate(l->l.onStartRuntime(execRt));
	}
	
	@Override
	public void onStopRuntime(ExecutionRuntime execRt) {
		delegate(l->l.onStopRuntime(execRt));
	}
	
	@Override
	public void onStartTxn(ExecutionTxnContext txnCtx) {
		delegate(l->l.onStartTxn(txnCtx));
	}

	@Override
	public void onEvent(ExecutionTxnContext txnCtx, ParamEvent event) {
		delegate(l->l.onEvent(txnCtx, event));
	}
	
	@Override
	public void onStopTxn(ExecutionTxnContext txnCtx) {
		Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents = aggregate(txnCtx);
		delegate(l->l.onStopTxn(txnCtx, aggregatedEvents));
	}
	
	@Override
	public void onStartRootCommandExecution(Command cmd) {
		delegate(l->l.onStartRootCommandExecution(cmd));
	}
	
	@Override
	public void onStopRootCommandExecution(Command cmd, ExecutionTxnContext txnCtx) {
		Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents = aggregate(txnCtx);
		delegate(l->l.onStopRootCommandExecution(cmd, aggregatedEvents));
		
		// reset thread local
		listenersInThread.get().clear();
	}
	
	@Override
	public void onStartCommandExecution(Command cmd) {
		delegate(l->l.onStartCommandExecution(cmd));
	}
	
	@Override
	public void onStopCommandExecution(Command cmd, ExecutionTxnContext txnCtx) {
		Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents = aggregate(txnCtx);
		delegate(l->l.onStopCommandExecution(cmd, aggregatedEvents));
	}
	
	private Map<ExecutionModel<?>, List<ParamEvent>> aggregate(ExecutionTxnContext txnCtx) {
		Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents = new HashMap<>();
		
		List<ParamEvent> events = txnCtx.getEvents();
		if(CollectionUtils.isEmpty(events))
			return aggregatedEvents;

		Set<Param<?>> unique = new HashSet<>(events.size());
		for(ParamEvent pe : events) {
			//ParamEvent pe = events.poll();
			
			List<ParamEvent> groupedEvents = createOrGet(aggregatedEvents, pe.getParam().getRootExecution());
			
			
			Param<?> colParent = findFirstCollectionParent(pe.getParam());
			ParamEvent resolved = (colParent==null) ? pe : new ParamEvent(pe.getAction(), colParent);

			if(!unique.contains(resolved.getParam())) {
				unique.add(resolved.getParam());
					
				groupedEvents.add(resolved);
			}

		}
		
		return aggregatedEvents;
	}

	private List<ParamEvent> createOrGet(Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents, ExecutionModel<?> root) {
		root = resolveRoot(root);
		
		if(aggregatedEvents.containsKey(root))
			return aggregatedEvents.get(root);
		
		List<ParamEvent> events = new ArrayList<>();
		aggregatedEvents.put(root, events);
		
		return events;
	}
	
	private ExecutionModel<?> resolveRoot(ExecutionModel<?> root) {
		if(root.getAssociatedParam().isLinked()) 
			return root.getAssociatedParam().findIfLinked().getRootExecution();
		
		return root;
	} 
	
	private static Param<?> findFirstCollectionParent(Param<?> currParam) {
		if(currParam.getParentModel()==null)
			return null;
		
		if(currParam.getParentModel().getAssociatedParam().isCollection())
			return currParam.getParentModel().getAssociatedParam();
		
		return findFirstCollectionParent(currParam.getParentModel().getAssociatedParam());
	}
	
	private void delegate(Consumer<StateEventListener> cb) {
		nullSafeGetDefault(cb);
		nullSafeGetTxn(cb);
	}
	
	private void nullSafeGetDefault(Consumer<StateEventListener> cb) {
		Optional.ofNullable(getDefaultScopedListeners())
			.ifPresent(list->list.forEach(cb));
	}
	
	private void nullSafeGetTxn(Consumer<StateEventListener> cb) {
		Optional.ofNullable(listenersInThread.get())
			.ifPresent(list->list.forEach(cb));
	}

}
