/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.collections.CollectionUtils;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.InvalidStateException;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.StateEventDelegator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor
public class DefaultExecutionRuntime implements ExecutionRuntime {
	
	private final Command rootCommand;
	
	private boolean isStarted;
	
	private final StateEventDelegator eventDelegator;
	
	private static final ThreadLocal<RuntimeScopes> runtimeScopesInThread = new ThreadLocal<RuntimeScopes>() {
		@Override
		protected RuntimeScopes initialValue() {
			return new RuntimeScopes();
		}
	};
	
	private static class RuntimeScopes {
		
		private final Map<ExecutionRuntime, ExecutionTxnContext> runtimeTxnContexts = new HashMap<>();
		
		public ExecutionTxnContext get(ExecutionRuntime rt) {
			return runtimeTxnContexts.get(rt);
		}
		
		public boolean containsValue(ExecutionRuntime rt) {
			if(!runtimeTxnContexts.containsKey(rt))
				return false;
			
			return runtimeTxnContexts.get(rt) != null;
		}
		
		public void create(ExecutionRuntime rt) {
			if(runtimeTxnContexts.containsKey(rt))
				throw new InvalidStateException("Remnant ExecRt found: "+rt);
			
			runtimeTxnContexts.put(rt, null);
		}
		
		public void remove(ExecutionRuntime rt) {
			if(!runtimeTxnContexts.containsKey(rt))
				throw new InvalidStateException("Expected ExecRt not found: "+rt);
			
			runtimeTxnContexts.remove(rt);
		}
		
		public void addTxnCtx(ExecutionRuntime rt, ExecutionTxnContext txnCtx) {
			if(!runtimeTxnContexts.containsKey(rt))
				throw new InvalidStateException("Expected ExecRt not found: "+rt);
			
			ExecutionTxnContext oldVal = runtimeTxnContexts.replace(rt, txnCtx);
			
			if(oldVal!=null)
				throw new InvalidStateException("Expected old TxnCtx entry for ExecRt to be null, but found: "+oldVal);
		}
		
		public ExecutionTxnContext removeTxnCtx(ExecutionRuntime rt) {
			if(!runtimeTxnContexts.containsKey(rt))
				throw new InvalidStateException("Expected ExecRt not found: "+rt);
			
			ExecutionTxnContext txnCtx = runtimeTxnContexts.get(rt);
			if(txnCtx==null)
				throw new InvalidStateException("Expected TxnCtx not found in runtime: "+rt);
			
			runtimeTxnContexts.replace(rt, null);
			return txnCtx;
		}
	}
	
	@Override
	public synchronized void start() {
		this.isStarted = true;
		runtimeScopesInThread.get().create(this);
		
		eventDelegator.onStartRuntime(this);
	}
	
	@Override
	public synchronized void stop() {
		this.isStarted = false;
		eventDelegator.onStopRuntime(this);
		
		runtimeScopesInThread.get().remove(this);
	}
	
	@Override
	public ExecutionTxnContext getTxnContext() {
		return runtimeScopesInThread.get().get(this);
	}
	
	@Override
	public void startTxn() {
		ExecutionTxnContext txnCtx = new DefaultExecutionTxnContext();
		runtimeScopesInThread.get().addTxnCtx(this, txnCtx);
		
		eventDelegator.onStartTxn(txnCtx);
	}
	
	public boolean isTxnStarted() {
		return runtimeScopesInThread.get().containsValue(this);
	}
	
	@Override
	public void stopTxn() {
		ExecutionTxnContext txnCtx = runtimeScopesInThread.get().get(this);
		eventDelegator.onStopTxn(txnCtx);
		
		runtimeScopesInThread.get().removeTxnCtx(this);
	}
	
	
	@Override
	public boolean isLocked(String lockId) {
		if(!isTxnStarted()) 
			return false;
		
		return getTxnContext().getId().equals(lockId);
	}
	
	@Override
	public String tryLock() {
		if(isTxnStarted()) 
			return null;
		
		startTxn();
		
		return getTxnContext().getId();
	}
	
	@Override
	public boolean tryUnlock(String lockId) {
		if(isLocked(lockId)) {
			stopTxn();
			return true;
		}
		
		return false;
	}
	
	@Override
	public void emitNotification(Notification<Object> notification) {
		getTxnContext().addNotification(notification);
	}
	
	@Override
	public void awaitNotificationsCompletion() {
		Queue<Notification<Object>> notifications = getTxnContext().getNotifications();
		if(CollectionUtils.isEmpty(notifications))
			return;

		while(!notifications.isEmpty()) {
			Notification<Object> event = notifications.poll();
			Param<Object> source =  event.getSource();
			
			if(CollectionUtils.isNotEmpty(source.getEventSubscribers()))
				new ArrayList<>(source.getEventSubscribers())
					.stream()
						.forEach(subscribedParam->subscribedParam.handleNotification(event));
		}
	}
	
	@Override
	public void emitEvent(ParamEvent event) {
		ExecutionTxnContext txnCtx = getTxnContext();
		txnCtx.addEvent(event);
		
		eventDelegator.onEvent(txnCtx, event);
	}
	
}
