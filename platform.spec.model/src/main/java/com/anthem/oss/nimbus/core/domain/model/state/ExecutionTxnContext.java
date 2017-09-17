/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.util.List;
import java.util.Queue;

/**
 * @author Soham Chakravarti
 *
 */
public interface ExecutionTxnContext {

	public boolean isLocked();
	public boolean isLocked(String lockId);
	
	public String tryLock();
	public boolean tryUnlock(String lockId);
	
	public void addNotification(Notification<Object> notification);
	public Queue<Notification<Object>> getNotifications();
	
	public void addEvent(ParamEvent event);
	public List<ParamEvent> getEvents();
}
