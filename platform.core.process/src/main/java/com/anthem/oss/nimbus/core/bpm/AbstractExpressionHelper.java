/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;

/**
 * @author Jayant.Chaudhuri
 *
 */
abstract public class AbstractExpressionHelper {
	
	private final CommandExecutorGateway executorGateway;
	
	public AbstractExpressionHelper(BeanResolverStrategy beanResolver) {
		this.executorGateway = beanResolver.get(CommandExecutorGateway.class);
	}
	

	final public Object executeProcess(CommandMessage cmdMsg){
		MultiOutput response = executorGateway.execute(cmdMsg);
		return response.getValue();
	}
	
}
