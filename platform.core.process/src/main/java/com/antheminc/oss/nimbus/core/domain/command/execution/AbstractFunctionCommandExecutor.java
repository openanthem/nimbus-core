/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.Behavior;
import com.antheminc.oss.nimbus.core.domain.command.CommandMessage;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.antheminc.oss.nimbus.core.domain.definition.Constants;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
public abstract class AbstractFunctionCommandExecutor<T,R> extends AbstractCommandExecutor<R> {

	public AbstractFunctionCommandExecutor(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	protected boolean containsFunctionHandler(Input input){
		String functionName = input.getContext().getCommandMessage().getCommand().getFirstParameterValue(Constants.KEY_FUNCTION.code);
		return (functionName != null);
	}
	
	protected <H extends FunctionHandler<T, R>> R executeFunctionHanlder(Input input, Class<H> handlerClass) {
		ExecutionContext eCtx = input.getContext();
		Param<T> actionParameter = findParamByCommandOrThrowEx(eCtx);
		H processHandler = getHandler(input.getContext().getCommandMessage(), handlerClass);
		return processHandler.execute(eCtx, actionParameter);
	}	
	
	protected <F extends FunctionHandler<?, ?>> F getHandler(CommandMessage commandMessage, Class<F> handlerClass){
		String functionName = constructFunctionHandlerKey(commandMessage);
		return getHandler(functionName, handlerClass);
	}	
	
	protected <F extends FunctionHandler<?, ?>> F getHandler(String functionName, Class<F> handlerClass){
		return findMatchingBean(handlerClass, functionName);
	}		
	
	private String constructFunctionHandlerKey(CommandMessage cmdMsg){
		StringBuilder key = new StringBuilder();
		String functionName = cmdMsg.getCommand().getFirstParameterValue(Constants.KEY_FUNCTION.code);
		String absoluteUri = cmdMsg.getCommand().getAbsoluteAlias();
		absoluteUri = absoluteUri.replaceAll(Constants.SEPARATOR_URI.code, "\\.");
		key.append(absoluteUri).append(".").append(cmdMsg.getCommand().getAction().toString())
		   .append(Behavior.$execute.name())
		   .append(Constants.REQUEST_PARAMETER_MARKER.code).append(Constants.KEY_FUNCTION.code).append("=").append(functionName);
		return key.toString();
	}	
	
}
