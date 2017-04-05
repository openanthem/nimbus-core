/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;

import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiDAO;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.PageNavigationInitializer;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultBehaviorExecutorNav extends DefaultActionExecutorNav {

	public DefaultBehaviorExecutorNav(ActivitiDAO platformProcessDAO, RuntimeService runtimeService,
			TaskService taskService, PageNavigationInitializer navigationStateHelper) {
		super(platformProcessDAO, runtimeService, taskService, navigationStateHelper);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
		QuadModel<?, ?> q = findQuad(cmdMsg);
		String navigationDirection = cmdMsg.getRawPayload();
		return (R)findNavigationPage(q, navigationDirection);
	}

}
