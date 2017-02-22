/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ParamStateBatchPersistenceEventListener;

/**
 * @author Rakesh Patel
 *
 */
@Component("default.$save")
public class DefaultBehaviorExecutorSave extends AbstractProcessTaskExecutor {

	//@Autowired
	ParamStateBatchPersistenceEventListener publisher;
	
	@Override
	public <R> R doExecuteInternal(CommandMessage cmdMsg) {
		
		//publisher.flush();
		
		return (R)Boolean.TRUE;
	}
	
	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {
		
	}
	
}
