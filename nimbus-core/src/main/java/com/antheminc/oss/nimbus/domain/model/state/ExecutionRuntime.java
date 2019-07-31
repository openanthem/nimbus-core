/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.model.state;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ExecutionModel;

/**
 * @author Soham Chakravarti
 *
 */
public interface ExecutionRuntime extends Notification.Dispatcher<Object> {

	public void start();
	public void stop();
	
	public boolean isStarted();
	
	public ExecutionModel<?> getRootExecution();
	
	/**
	 * Wait till registered fine-grained state changes are notified to consumers and they have completed handling the event
	 */
	public void awaitNotificationsCompletion();

	public StateEventDelegator getEventDelegator();
	
	public void emitEvent(ParamEvent event);
	
	public ExecutionTxnContext getTxnContext();
	
	public <R> R executeInLock(BiFunction<ExecutionTxnContext, String, R> cb);
	public void executeInLock(BiConsumer<ExecutionTxnContext, String> cb);
	
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
