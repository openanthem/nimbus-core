/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

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
