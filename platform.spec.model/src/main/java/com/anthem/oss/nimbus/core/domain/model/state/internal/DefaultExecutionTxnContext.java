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

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor
public class DefaultExecutionTxnContext implements ExecutionTxnContext {
	
	private final String id = UUID.randomUUID().toString();

	private final BlockingQueue<Notification<Object>> notifications = new LinkedBlockingQueue<>();
	
	private final BlockingQueue<ParamEvent> events = new LinkedBlockingQueue<>();
	
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
