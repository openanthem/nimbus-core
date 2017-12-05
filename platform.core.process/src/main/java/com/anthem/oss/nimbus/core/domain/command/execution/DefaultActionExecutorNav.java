/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.CommandMessage;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.antheminc.oss.nimbus.core.domain.command.execution.nav.NavigationHandler;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionExecutorNav<T> extends AbstractFunctionCommandExecutor<T,String> {
	
	public DefaultActionExecutorNav(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Output<String> executeInternal(Input input) {
		String pageId = (String)executeFunctionHanlder(input, NavigationHandler.class);
		return Output.instantiate(input, input.getContext(), pageId);		
	}
	
	@Override
	protected <F extends FunctionHandler<?, ?>> F getHandler(CommandMessage commandMessage, Class<F> handlerClass) {
		F handler = super.getHandler(commandMessage, handlerClass);
		if(handler == null){
			handler = super.getHandler("default._nav$execute?fn=default",handlerClass);
		}
		return handler;
	}
	
}