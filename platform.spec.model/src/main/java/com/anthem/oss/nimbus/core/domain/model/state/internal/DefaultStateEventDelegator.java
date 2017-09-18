/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;

import com.anthem.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.StateEventDelegator;
import com.anthem.oss.nimbus.core.domain.model.state.StateEventListener;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class DefaultStateEventDelegator implements StateEventDelegator {
	
	private List<StateEventListener> defaultScopedListeners;
	
	private static final ThreadLocal<List<StateEventListener>> txnScopedListeners = new ThreadLocal<List<StateEventListener>>() {
		@Override
		protected List<StateEventListener> initialValue() {
			return new ArrayList<>();
		}
	};

	@Override
	public void addTxnScopedListener(StateEventListener listener) {
		txnScopedListeners.get().add(listener);
	}
	
	@Override
	public boolean removeTxnScopedListener(StateEventListener listener) {
		return txnScopedListeners.get().remove(listener);
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
	public void onEvent(ParamEvent event) {
		delegate(l->l.onEvent(event));
	}
	
	@Override
	public void onStopTxn(ExecutionTxnContext txnCtx) {
		Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents = new HashMap<>();
		aggregate(txnCtx, aggregatedEvents);
		
		delegate(l->l.onStopTxn(txnCtx, aggregatedEvents));
		
		// reset at stop
		txnScopedListeners.set(new ArrayList<>());
	}
	
	private void aggregate(ExecutionTxnContext txnCtx, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents) {
		Queue<ParamEvent> events = txnCtx.getEvents();
		if(CollectionUtils.isEmpty(events))
			return;

		Set<Model<?>> unique = new HashSet<>(events.size());
		while(!events.isEmpty()) {
			ParamEvent pe = events.poll();
			
			List<ParamEvent> groupedEvents = createOrGet(aggregatedEvents, pe.getParam().getRootExecution());
			
			if(!pe.getParam().isCollectionElem()) {
				groupedEvents.add(pe);
				
			} else {
				Model<?> colModel = pe.getParam().findIfCollectionElem().getParentModel();
				if(!unique.contains(colModel)) {
					unique.add(colModel);
					
					groupedEvents.add(new ParamEvent(pe.getAction(), pe.getParam().getParentModel().getAssociatedParam()));
				}
			}

		}
	}

	private List<ParamEvent> createOrGet(Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents, ExecutionModel<?> root) {
		if(aggregatedEvents.containsKey(root))
			return aggregatedEvents.get(root);
		
		List<ParamEvent> events = new ArrayList<>();
		aggregatedEvents.put(root, events);
		
		return events;
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
		Optional.ofNullable(txnScopedListeners.get())
			.ifPresent(list->list.forEach(cb));
	}

}
