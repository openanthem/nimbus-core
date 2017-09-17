/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

/**
 * @author Soham Chakravarti
 *
 */
public interface ExecutionRuntime extends ExecutionTxnContext, Notification.Dispatcher<Object> {

	public void start();
	public void stop();
	
	public boolean isStarted();
	
	public void awaitCompletion();

}
