/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state;

import java.util.List;
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
	public List<ParamEvent> getEvents();
}
