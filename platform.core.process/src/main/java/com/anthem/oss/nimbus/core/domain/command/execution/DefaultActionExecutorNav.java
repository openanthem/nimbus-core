/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.nimbus.platform.core.function.handler.FunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionExecutorNav extends DefaultActionExecutorProcess {
	
	@Override
	@SuppressWarnings("unchecked")
	protected String doExecuteInternal(CommandMessage cmdMsg) {
		String pageId = (String)super.doExecuteInternal(cmdMsg);
		return pageId;
	}
	
	@Override
	public FunctionHandler getHandler(CommandMessage commandMessage) {
		FunctionHandler functionHandler = super.getHandler(commandMessage);
		if(functionHandler == null){
			return appCtx.getBean("defaultNavigationHandler", FunctionHandler.class);
		}
		return functionHandler;
	}
}