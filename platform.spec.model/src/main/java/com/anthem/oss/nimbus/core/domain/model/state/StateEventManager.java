/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.util.Queue;

/**
 * @author Soham Chakravarti
 *
 */
public interface StateEventManager {

	public void dispatchNotifications(Queue<Notification<Object>> notifications);
	
	public void dispatchEvents(Queue<ParamEvent> events);
}
