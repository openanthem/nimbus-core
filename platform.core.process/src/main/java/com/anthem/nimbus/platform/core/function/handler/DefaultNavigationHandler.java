/**
 * 
 */
package com.anthem.nimbus.platform.core.function.handler;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionContext;

/**
 * @author Jayant Chaudhuri
 *
 */
public class DefaultNavigationHandler implements FunctionHandler {
	
	public static final String PAGE_ID="pageId";

	/* (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.HierarchyMatch#getUri()
	 */
	@Override
	public String getUri() {
		return "defaultNavigationHandler";
	}

	/* (non-Javadoc)
	 * @see com.anthem.nimbus.platform.core.function.handler.FunctionHandler#executeProcess(com.anthem.oss.nimbus.core.domain.model.state.ExecutionContext, com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <R> R executeProcess(ExecutionContext executionContext, Param<?> actionParameter) {
		return (R)executionContext.getCommandMessage().getCommand().getFirstParameterValue(PAGE_ID);
	}

}
