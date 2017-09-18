/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.ArrayList;
import java.util.Queue;

import org.apache.commons.collections.CollectionUtils;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
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
	
	private static final ThreadLocal<ExecutionTxnContext> txnContext = new ThreadLocal<ExecutionTxnContext>();
	
	@Override
	public synchronized void start() {
		this.isStarted = true;
		eventDelegator.onStartRuntime(this);
	}
	
	@Override
	public synchronized void stop() {
		this.isStarted = false;
		eventDelegator.onStopRuntime(this);
	}
	
	@Override
	public ExecutionTxnContext getTxnContext() {
		return txnContext.get();
	}
	
	@Override
	public void startTxn() {
		txnContext.set(new DefaultExecutionTxnContext());
		eventDelegator.onStartTxn(txnContext.get());
	}
	
	public boolean isTxnStarted() {
		return txnContext.get()!=null;
	}
	
	@Override
	public void stopTxn() {
		ExecutionTxnContext txnCtx = txnContext.get();
		txnContext.set(null);
		eventDelegator.onStopTxn(txnCtx);
	}
	
	
	@Override
	public boolean isLocked(String lockId) {
		if(!isTxnStarted()) 
			return false;
		
		return txnContext.get().getId().equals(lockId);
	}
	
	@Override
	public String tryLock() {
		if(isTxnStarted()) 
			return null;
		
		startTxn();
		
		return txnContext.get().getId();
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
		txnContext.get().addNotification(notification);
	}
	
	@Override
	public void awaitNotificationsCompletion() {
		Queue<Notification<Object>> notifications = txnContext.get().getNotifications();
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
		txnContext.get().addEvent(event);
		
		eventDelegator.onEvent(event);
	}
	
}
