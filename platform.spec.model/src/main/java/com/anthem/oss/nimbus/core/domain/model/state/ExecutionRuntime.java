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
	
	/**
	 * Wait till registered fine-grained state changes are notified to consumers and they have completed handling the event
	 */
	public void awaitCompletion();

	/**
	 * Publishes coarse events to registered subscribers
	 */
	public void publishEvents();
}
