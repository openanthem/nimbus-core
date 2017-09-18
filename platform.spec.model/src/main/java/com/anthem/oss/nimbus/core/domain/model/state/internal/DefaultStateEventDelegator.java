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

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.InvalidStateException;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.StateEventDelegator;
import com.anthem.oss.nimbus.core.domain.model.state.StateEventListener;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class DefaultStateEventDelegator implements StateEventDelegator {
	
	private List<StateEventListener> defaultScopedListeners;
	
	private static final ThreadLocal<TxnScopes> txnScopesInThread = new ThreadLocal<TxnScopes>() {
		@Override
	 	protected TxnScopes initialValue() {
			return new TxnScopes();
		}
	};

	@ToString
	private static class TxnScopes {
		@Getter
		private final List<StateEventListener> accumulatedListeners = new ArrayList<>();
		
		private final Map<ExecutionTxnContext, List<StateEventListener>> txnListenerCtx = new HashMap<>();
		
		public void throwExIfInvalidState() {
			if(CollectionUtils.isNotEmpty(getAccumulatedListeners()))
				throw new InvalidStateException("Accumulated listeners should have been drained, but found non-empty: "+getAccumulatedListeners());
			
			if(!txnListenerCtx.isEmpty())
				throw new InvalidStateException("TxnScope listeners should have been removed, but found non-empty: "+txnListenerCtx);
		}
		
		public List<StateEventListener> get(ExecutionTxnContext txnCtx) {
			return txnListenerCtx.get(txnCtx);
		}
		
		public List<StateEventListener> createAndAdd(ExecutionTxnContext txnCtx) {
			if(txnListenerCtx.containsKey(txnCtx))
				throw new InvalidStateException("Expected listeners for txnCtx: "+txnCtx+" to be empty, but found: "+get(txnCtx));
			
			// add accumulated to new txnScope
			List<StateEventListener> txnListeners = new ArrayList<>();
			txnListeners.addAll(getAccumulatedListeners());
			
			// drain accumulated
			getAccumulatedListeners().clear();
			
			txnListenerCtx.put(txnCtx, txnListeners);
			return txnListeners;
		}
		
		public void remove(ExecutionTxnContext txnCtx) {
			if(!txnListenerCtx.containsKey(txnCtx))
				throw new InvalidStateException("Expected to find listeners for txnCtx: "+txnCtx);
			
			txnListenerCtx.remove(txnCtx);
		}
	}
	
	
	@Override
	public void addTxnScopedListener(StateEventListener listener) {
		txnScopesInThread.get().getAccumulatedListeners().add(listener);
	}
	
	@Override
	public boolean removeTxnScopedListener(StateEventListener listener) {
		return txnScopesInThread.get().getAccumulatedListeners().remove(listener);
	}
	
	@Override
	public void onStartRuntime(ExecutionRuntime execRt) {
		txnScopesInThread.get().throwExIfInvalidState();
		
		nullSafeGetDefault(l->l.onStartRuntime(execRt));
	}
	
	@Override
	public void onStopRuntime(ExecutionRuntime execRt) {
		nullSafeGetDefault(l->l.onStopRuntime(execRt));
		
		txnScopesInThread.get().throwExIfInvalidState();
	}
	
	@Override
	public void onStartTxn(ExecutionTxnContext txnCtx) {
		List<StateEventListener> txnListeners = txnScopesInThread.get().createAndAdd(txnCtx);
		delegate(txnListeners, l->l.onStartTxn(txnCtx));
	}

	@Override
	public void onEvent(ExecutionTxnContext txnCtx, ParamEvent event) {
		List<StateEventListener> txnListeners = txnScopesInThread.get().get(txnCtx);
		delegate(txnListeners, l->l.onEvent(txnCtx, event));
	}
	
	@Override
	public void onStopTxn(ExecutionTxnContext txnCtx) {
		Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents = new HashMap<>();
		aggregate(txnCtx, aggregatedEvents);
		
		List<StateEventListener> txnListeners = txnScopesInThread.get().get(txnCtx);
		delegate(txnListeners, l->l.onStopTxn(txnCtx, aggregatedEvents));
		
		// reset at stop
		txnScopesInThread.get().remove(txnCtx);
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
	
	private void delegate(List<StateEventListener> txnListeners, Consumer<StateEventListener> cb) {
		nullSafeGetDefault(cb);
		
		nullSafeGetTxn(txnListeners, cb);
	}
	
	private void nullSafeGetDefault(Consumer<StateEventListener> cb) {
		Optional.ofNullable(getDefaultScopedListeners())
			.ifPresent(list->list.forEach(cb));
	}
	
	private void nullSafeGetTxn(List<StateEventListener> txnListeners, Consumer<StateEventListener> cb) {
		Optional.ofNullable(txnListeners)
			.ifPresent(list->list.forEach(cb));
	}

}
