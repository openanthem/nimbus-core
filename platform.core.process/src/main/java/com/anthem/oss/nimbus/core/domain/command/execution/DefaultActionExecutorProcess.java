/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.domain.command.CommandMessage;

/**
 * @author Jayant Chaudhuri
 *
 */
public class DefaultActionExecutorProcess extends AbstractProcessTaskExecutor {
	
	@Override
	@SuppressWarnings("unchecked")
	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
		return (R)doExecuteFunctionHandler(cmdMsg, FunctionHandler.class);
	}

}
