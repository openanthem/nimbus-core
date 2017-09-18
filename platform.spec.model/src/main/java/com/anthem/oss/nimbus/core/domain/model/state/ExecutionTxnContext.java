/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.util.Queue;

/**
 * @author Soham Chakravarti
 *
 */
public interface ExecutionTxnContext {

	public String getId();
	
	public void addNotification(Notification<Object> notification);
	public Queue<Notification<Object>> getNotifications();
	
	public void addEvent(ParamEvent event);
	public Queue<ParamEvent> getEvents();
}
