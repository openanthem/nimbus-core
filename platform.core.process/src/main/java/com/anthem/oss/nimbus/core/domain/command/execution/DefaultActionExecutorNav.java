/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.command.execution.nav.NavigationHandler;

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