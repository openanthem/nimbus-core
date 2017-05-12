/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.nimbus.platform.core.function.handler.FunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

/**
 * @author Jayant Chaudhuri
 *
 */
public class DefaultActionExecutorProcess extends AbstractProcessTaskExecutor {
	
	@Override
	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
		QuadModel<?, ?> q = findQuad(cmdMsg);
		
		//TODO: Load action parameter based on Command
		Param<?> actionParameter = null;
		ExecutionContext executionContext = new ExecutionContext(cmdMsg,q);
		FunctionHandler processHandler = getHandler(cmdMsg);
		
		@SuppressWarnings("unchecked")
		R response = (R)processHandler.executeProcess(executionContext, actionParameter);
		return response ;
	}
	
	public FunctionHandler getHandler(CommandMessage commandMessage){
		String functionName = commandMessage.getCommand().getFirstParameterValue(FunctionHandler.FUNCTION_ID);
		return appCtx.getBean(functionName, FunctionHandler.class);
	}	
}
