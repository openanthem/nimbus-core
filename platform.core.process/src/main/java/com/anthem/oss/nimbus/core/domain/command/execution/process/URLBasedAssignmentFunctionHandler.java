/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.process;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.AbstractProcessGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.MultiExecuteOutput;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
abstract public class URLBasedAssignmentFunctionHandler<T,R,S> implements FunctionHandler<T,R> {
	
	private AbstractProcessGateway processGateway; 
	
	@Override
	public R execute(ExecutionContext executionContext, Param<T> actionParameter) {
		CommandMessage commandFromContext =  buildCommand(executionContext.getCommandMessage());
		Param<S> targetParameterState = findTargetParam(executionContext);
		S state = isInternal(commandFromContext.getCommand()) ? getInternalState(executionContext): getExternalState(executionContext);
		return assign(executionContext,actionParameter,targetParameterState,state);
	}
	
	abstract public R assign(ExecutionContext executionContext, Param<T> actionParameter,Param<S> targetParameter, S state);

	protected String getUrl(CommandMessage commandMessage){
		return commandMessage.getCommand().getRequestParams().get("url")[0];
	}
	
	protected CommandMessage buildCommand(CommandMessage commandMessage){
		String url = getUrl(commandMessage);
		Command command = CommandBuilder.withUri(url).getCommand();
		command.setRequestParams(commandMessage.getCommand().getRequestParams());
		CommandMessage newCommandMessage = new CommandMessage();
		newCommandMessage.setCommand(command);
		return newCommandMessage;
	}
	
	//TODO: Verify logic
	protected boolean isInternal(Command command){
		return command.getAlias(Type.PlatformMarker) == null;
	}
	
	protected Param<S> findTargetParam(ExecutionContext context){
		String parameterPath = context.getCommandMessage().getCommand().getAbsoluteUri();
		return context.getRootModel().findParamByPath(parameterPath);
	}	
	
	protected S getInternalState(ExecutionContext executionContext){
		String url = getUrl(executionContext.getCommandMessage());
		Param<S> sourceParameter = executionContext.getRootModel().findParamByPath(url);
		return sourceParameter.getState();
	}
	
	protected S getExternalState(ExecutionContext executionContext){
		CommandMessage commandToExecute = buildCommand(executionContext.getCommandMessage());
		MultiExecuteOutput response = (MultiExecuteOutput)processGateway.executeProcess(commandToExecute).getResponse();
		return response.getSingleResult();
	}	

}
