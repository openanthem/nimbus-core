/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.List;
import java.util.Queue;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.StateEventManager;

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
	
	private final StateEventManager eventManager = new DefaultStateEventManager();
	
	private static final ThreadLocal<ExecutionTxnContext> txnContext = new ThreadLocal<ExecutionTxnContext>() {
		@Override
		protected ExecutionTxnContext initialValue() {
			return new DefaultExecutionTxnContext();
		}
	};
	
	@Override
	public synchronized void start() {
		this.isStarted = true;
	}
	
	@Override
	public synchronized void stop() {
		this.isStarted = false;
	}
	
	@Override
	public boolean isLocked() {
		return txnContext.get().isLocked();
	}
	
	@Override
	public boolean isLocked(String lockId) {
		return txnContext.get().isLocked(lockId);
	}
	
	@Override
	public String tryLock() {
		return txnContext.get().tryLock();
	}
	
	@Override
	public boolean tryUnlock(String lockId) {
		return txnContext.get().tryUnlock(lockId);
	}
	
	@Override
	public void addEvent(ParamEvent event) {
		txnContext.get().addEvent(event);
	}
	
	@Override
	public List<ParamEvent> getEvents() {
		return txnContext.get().getEvents();
	}
	
	@Override
	public void addNotification(Notification<Object> notification) {
		txnContext.get().addNotification(notification);
	}
	
	@Override
	public Queue<Notification<Object>> getNotifications() {
		return txnContext.get().getNotifications();
	}
	
	@Override
	public void awaitCompletion() {
		eventManager.dispatchNotifications(txnContext.get().getNotifications());
	}
	
}
