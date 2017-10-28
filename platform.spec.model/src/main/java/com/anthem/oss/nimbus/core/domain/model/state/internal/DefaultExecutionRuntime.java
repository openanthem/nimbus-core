/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.Iterator;
import java.util.Queue;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.apache.commons.collections.CollectionUtils;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.MappedParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.InvalidStateException;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.StateEventDelegator;
import com.anthem.oss.nimbus.core.util.JustLogit;

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
			
				boolean b = tryUnlock(lockId);
				if(!b)
					throw new FrameworkRuntimeException("Failed to release lock acquired during txn execution of runtime: "+this+" with acquired lockId: "+lockId);
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
				
				boolean b = tryUnlock(lockId);
				if(!b)
					throw new FrameworkRuntimeException("Failed to release lock acquired during txn execution of runtime: "+this+" with acquired lockId: "+lockId);
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
		Queue<Notification<Object>> notifications = getTxnContext().getNotifications();
		if(CollectionUtils.isEmpty(notifications))
			return;

		while(!notifications.isEmpty()) {
			Notification<Object> event = notifications.poll();
			Param<Object> source =  event.getSource();
			
			Iterator<MappedParam<?, Object>> iter = source.getEventSubscribers();
			if(iter!=null && iter.hasNext())
				iter.forEachRemaining(subscribedParam->subscribedParam.handleNotification(event));
			
//			if(CollectionUtils.isNotEmpty(source.getEventSubscribers()))
//				new ArrayList<>(source.getEventSubscribers())
//					.stream()
//						.forEach(subscribedParam->subscribedParam.handleNotification(event));
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
