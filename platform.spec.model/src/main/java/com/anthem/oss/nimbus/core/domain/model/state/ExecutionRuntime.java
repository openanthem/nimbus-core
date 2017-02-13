/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

/**
 * @author Soham Chakravarti
 *
 */
public interface ExecutionRuntime extends Notification.Dispatcher<Object> {

	public void start();
	public void stop();
	
	public void awaitCompletion();
	
	public boolean isLocked();
	public boolean isLocked(String lockId);
	public String tryLock();
	public boolean tryUnlock(String lockId);
}
