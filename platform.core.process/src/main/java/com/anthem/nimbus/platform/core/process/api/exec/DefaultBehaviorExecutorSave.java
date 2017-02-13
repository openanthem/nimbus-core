/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.exec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.domain.event.ParamStateBatchPersistenceEventPublisher;
import com.anthem.nimbus.platform.spec.contract.process.ProcessExecutorEvents;
import com.anthem.oss.nimbus.core.domain.CommandMessage;

/**
 * @author Rakesh Patel
 *
 */
@Component("default.$save")
public class DefaultBehaviorExecutorSave extends AbstractProcessTaskExecutor {

	@Autowired
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
