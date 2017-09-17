/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.util.LockTemplate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor
public class DefaultExecutionTxnContext implements ExecutionTxnContext {
	
	private String lockId;
	private final LockTemplate lock = new LockTemplate();

	private final BlockingQueue<Notification<Object>> notifications = new LinkedBlockingQueue<>();
	
	private final BlockingQueue<ParamEvent> events = new LinkedBlockingQueue<>();
	
	@Override
	public String tryLock() {
		if(isLocked()) return null;
		
		return getLock().execute(()-> {
			this.lockId = UUID.randomUUID().toString();
			return getLockId();
		});
	}

	@Override
	public boolean isLocked() {
		return (getLockId()!=null);
	}
	
	@Override
	public boolean isLocked(String lockId) {
		if(!isLocked()) return false;
		
		return getLockId().equals(lockId);
	}
	
	@Override
	public boolean tryUnlock(String lockId) {
		if(!isLocked(lockId)) return true;
		
		return getLock().execute(()-> {
			if(isLocked(lockId)) {
				this.lockId = null;
				return true;
			}
			
			return false;
		});
	}
	
	@Override
	public void addNotification(Notification<Object> notification) {
		try {
			getNotifications().put(notification);
		} catch (InterruptedException ex) {
			throw new FrameworkRuntimeException("Failed to place notification event on queue with value: "+notification, ex);
		}	
	}

	
	@Override
	public void addEvent(ParamEvent event) {
		getEvents().add(event);
	}
	
}
