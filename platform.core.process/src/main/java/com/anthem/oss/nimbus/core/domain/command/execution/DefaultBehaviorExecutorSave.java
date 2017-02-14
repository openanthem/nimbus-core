/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.domain.event.ParamStateBatchPersistenceEventPublisher;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;

/**
 * @author Rakesh Patel
 *
 */
@Component("default.$save")
public class DefaultBehaviorExecutorSave extends AbstractProcessTaskExecutor {

	//@Autowired
	ParamStateBatchPersistenceEventPublisher publisher;
	
	@Override
	public <R> R doExecuteInternal(CommandMessage cmdMsg) {
		
		publisher.flush();
		
		return (R)Boolean.TRUE;
	}
	
	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {
		
	}
	
}
