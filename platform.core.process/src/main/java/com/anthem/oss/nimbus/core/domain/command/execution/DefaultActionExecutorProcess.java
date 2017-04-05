/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiGateway;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

/**
 * @author Jayant Chaudhuri
 *
 */
public class DefaultActionExecutorProcess extends AbstractProcessTaskExecutor {
	
	ActivitiGateway activitiProcessGateway;
	
	HierarchyMatchBasedBeanFinder hierarchyMatchBeanLoader;
	
	public static final String QUAD_MODEL_KEY = "quadModel";
	
	public DefaultActionExecutorProcess(ActivitiGateway activitiProcessGateway,
			HierarchyMatchBasedBeanFinder hierarchyMatchBeanLoader) {
		this.activitiProcessGateway = activitiProcessGateway;
		this.hierarchyMatchBeanLoader = hierarchyMatchBeanLoader;
	}
	
	
	@Override
	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
		QuadModel<?, ?> q = findQuad(cmdMsg);
		
//		FlowState processState = q.getFlow().getState();
//		String processExecutionId = processState.getProcessExecutionId();
		String processExecutionId = (String)q.getFlow().findStateByPath("/processExecutionId").getState();
		if(processExecutionId != null){
			Object resp = activitiProcessGateway.continueProcessExecution(cmdMsg,q,processExecutionId);
			return (R)resp;
		}
		
		Object resp = activitiProcessGateway.initiateProcessExecution(cmdMsg,q);
		return (R)resp;
	}
}
