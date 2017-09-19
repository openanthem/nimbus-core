/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.ArrayList;
import java.util.Queue;
import java.util.UUID;

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
	
	private static final ThreadLocal<DefaultExecutionTxnContext> txnScopeInThread = new ThreadLocal<DefaultExecutionTxnContext>() {
		@Override
		protected DefaultExecutionTxnContext initialValue() {
			return new DefaultExecutionTxnContext();
		}
	};
	
	
	@Override
	public synchronized void start() {
//		if(isTxnStarted())
//			throw new InvalidStateException("Expected to not any trailing txn context, but found one: "+getTxnContext());
		
		this.isStarted = true;
		eventDelegator.onStartRuntime(this);
	}
	
	@Override
	public synchronized void stop() {
		eventDelegator.onStopRuntime(this);
		this.isStarted = false;
	}
	
	@Override
	public DefaultExecutionTxnContext getTxnContext() {
		return txnScopeInThread.get();
	}
	
	
	@Override
	public void startTxn() {
		if(isTxnStarted())
			throw new InvalidStateException("Txn already started with id: "+getTxnContext().getId());
		
		String lockId = UUID.randomUUID().toString();
		getTxnContext().setId(lockId);
		
		eventDelegator.onStartTxn(getTxnContext());
	}
	
	public boolean isTxnStarted() {
		return getTxnContext().getId() != null;
	}
	
	@Override
	public void stopTxn() {
		if(!isTxnStarted())
			throw new InvalidStateException("Txn not started to stop.");
		
		eventDelegator.onStopTxn(getTxnContext());
		
		getTxnContext().setId(null);
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

	@Override
	public void onStartRootCommandExecution(Command cmd) {
		txnScopeInThread.set(new DefaultExecutionTxnContext());
		eventDelegator.onStartRootCommandExecution(cmd);
	}
	
	@Override
	public void onStopRootCommandExecution(Command cmd) {
		eventDelegator.onStopRootCommandExecution(cmd, getTxnContext());
		txnScopeInThread.set(null);
	}
	
	@Override
	public void onStartCommandExecution(Command cmd) {
		// TODO change events based command lifecycle
		txnScopeInThread.get().getEvents().clear();
		
		eventDelegator.onStartCommandExecution(cmd);	
	}
	
	@Override
	public void onStopCommandExecution(Command cmd) {
		eventDelegator.onStopCommandExecution(cmd, getTxnContext());
		
		// TODO change events based command lifecycle
		txnScopeInThread.get().getEvents().clear();
	}
}
