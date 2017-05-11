/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

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

}