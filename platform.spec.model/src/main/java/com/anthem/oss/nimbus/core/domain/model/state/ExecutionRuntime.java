/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import com.anthem.oss.nimbus.core.domain.command.Command;

/**
 * @author Soham Chakravarti
 *
 */
public interface ExecutionRuntime extends Notification.Dispatcher<Object> {

	public void start();
	public void stop();
	
	public boolean isStarted();
	
	/**
	 * Wait till registered fine-grained state changes are notified to consumers and they have completed handling the event
	 */
	public void awaitNotificationsCompletion();

	public StateEventDelegator getEventDelegator();
	
	public void emitEvent(ParamEvent event);
	
	public ExecutionTxnContext getTxnContext();
	
	public void startTxn();
	public void stopTxn();
	
	public boolean isLocked(String lockId);
	
	public String tryLock();
	public boolean tryUnlock(String lockId);

	
	public void onStartRootCommandExecution(Command cmd);
	public void onStopRootCommandExecution(Command cmd);
	
	public void onStartCommandExecution(Command cmd);
	public void onStopCommandExecution(Command cmd);
}
