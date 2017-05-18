/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.nav.NavigationHandler;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionExecutorNav extends AbstractProcessTaskExecutor {
	
	
	
	@Override
	@SuppressWarnings("unchecked")
	protected String doExecuteInternal(CommandMessage cmdMsg) {
		return doExecuteFunctionHandler(cmdMsg, NavigationHandler.class);
	}
	
	@Override
	protected <T extends FunctionHandler<?, ?>> T getHandler(CommandMessage commandMessage, Class<T> handlerClass) {
		T handler = super.getHandler(commandMessage, handlerClass);
		if(handler == null){
			handler = super.getHandler("default._nav$execute?fn=default",handlerClass);
		}
		return handler;
	}
	
}