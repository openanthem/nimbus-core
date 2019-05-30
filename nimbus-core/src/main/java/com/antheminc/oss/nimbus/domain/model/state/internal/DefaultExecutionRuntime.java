/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.model.state.internal;

import java.util.ArrayList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.apache.commons.collections.CollectionUtils;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionRuntime;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.domain.model.state.InvalidStateException;
import com.antheminc.oss.nimbus.domain.model.state.Notification;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.domain.model.state.StateEventDelegator;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor @ToString(of="rootCommand")
public class DefaultExecutionRuntime implements ExecutionRuntime {

	public static final String UNLOCK_FAILURE_ERROR_MSG = "Failed to release lock acquired during txn execution of runtime: %s with acquired lockId: %s";
	
	private final Command rootCommand;
	private final StateEventDelegator eventDelegator;

	@Setter
	private ExecutionModel<?> rootExecution;
	
	private boolean isStarted;
	
	protected JustLogit logit = new JustLogit(this.getClass());
	
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
		
		logit.trace(()->"Started txn with lockId: "+lockId);
	}
	
	public boolean isTxnStarted() {
		return getTxnContext()!=null && getTxnContext().getId() != null;
	}
	
	@Override
	public void stopTxn() {
		if(!isTxnStarted())
			throw new InvalidStateException("Txn not started to stop.");
		
		eventDelegator.onStopTxn(getTxnContext());
		
		String lockId = getTxnContext().getId();
		getTxnContext().setId(null);
		
		logit.trace(()->"Stopped txn with lockId: "+lockId);
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
	public <R> R executeInLock(BiFunction<ExecutionTxnContext, String, R> cb) {
		String lockId = tryLock();
		try {
			return cb.apply(getTxnContext(), lockId);
		} finally {
			
			if(isLocked(lockId)) {
				if (!tryUnlock(lockId)) {
					logit.error(() -> String.format(UNLOCK_FAILURE_ERROR_MSG, this, lockId));
				}
			}
		}
	}
	
	@Override
	public void executeInLock(BiConsumer<ExecutionTxnContext, String> cb) {
		String lockId = tryLock();
		try {
			cb.accept(getTxnContext(), lockId);
		} finally {
			if(isLocked(lockId)) {
				
				if (!tryUnlock(lockId)) {
					logit.error(() -> String.format(UNLOCK_FAILURE_ERROR_MSG, this, lockId));
				}
			}
		}
	}
	
	@Override
	public void emitNotification(Notification<Object> notification) {
		getTxnContext().addNotification(notification);
	}
	
	@Override
	public final void awaitNotificationsCompletion() {
		executeInLock((txnCtx, lockId)->{awaitNotificationsCompletionInternal();});
	}
	
	protected void awaitNotificationsCompletionInternal() {
		Queue<Notification<Object>> _notifications = getTxnContext().getNotifications();
		if(CollectionUtils.isEmpty(_notifications))
			return;

		// copy
		Queue<Notification<Object>> notifications = new LinkedBlockingDeque<>(_notifications);
		
		while(!notifications.isEmpty()) {
			Notification<Object> event = notifications.poll();
			_notifications.remove(event);
			Param<Object> source =  event.getSource();
			
			if(CollectionUtils.isNotEmpty(source.getEventSubscribers()))
				new ArrayList<>(source.getEventSubscribers())
					.stream()
						.forEach(subscribedParam->subscribedParam.handleNotification(event));
		}
		
		int initialSize = _notifications.size();
		int finalSize = notifications.size();
		
		if(initialSize!=finalSize) {
			if(logit.getLog().isDebugEnabled())
				logit.info(()->"[awaitNotificationsCompletion] re-invoke to handle additional events added during initial notifications processing in txnCtx: "
						+getTxnContext().getId()+" with notification queue size initial: "+initialSize+" final: "+finalSize);
		
			awaitNotificationsCompletion();
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
		eventDelegator.onStartRootCommandExecution(cmd);
	}
	
	@Override
	public void onStopRootCommandExecution(Command cmd) {
		eventDelegator.onStopRootCommandExecution(cmd, getTxnContext());
		txnScopeInThread.set(new DefaultExecutionTxnContext());
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
