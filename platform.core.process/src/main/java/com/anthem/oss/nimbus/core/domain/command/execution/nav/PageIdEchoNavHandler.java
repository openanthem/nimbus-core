/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.nav;

import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
//default._nav$execute?fn=default
public class PageIdEchoNavHandler<T> implements NavigationHandler<T> {

	@Override
	public String execute(ExecutionContext executionContext, Param<T> actionParameter) {
		return executionContext.getCommandMessage().getCommand().getFirstParameterValue(Constants.KEY_NAV_ARG_PAGE_ID.code);
	}

	
}
