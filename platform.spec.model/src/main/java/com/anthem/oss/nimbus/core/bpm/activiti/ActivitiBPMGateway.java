/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.anthem.oss.nimbus.core.bpm.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.anthem.nimbus.platform.spec.model.process.ProcessEngineContext;
import com.anthem.oss.nimbus.core.bpm.BPMGateway;
import com.anthem.oss.nimbus.core.domain.expr.ExpressionEvaluator;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.ProcessResponse;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.internal.ExecutionEntity;

/**
 * @author Jayant Chaudhuri
 *
 */
public class ActivitiBPMGateway implements BPMGateway {
	
	@Autowired RuntimeService runtimeService;
	
	@Autowired TaskService taskService;
	
	@Autowired SpringProcessEngineConfiguration processEngineConfiguration;
	
	private ExpressionEvaluator expressionEvaluator;
	
	public ActivitiBPMGateway (BeanResolverStrategy beanResolver) {
		this.expressionEvaluator = beanResolver.find(ExpressionEvaluator.class);
	}
	
	@Override
	public ActivitiProcessFlow startBusinessProcess(Param<?> param, String processId) {
		ProcessResponse processResponse = startStatlessBusinessProcess(param, processId);
		ActivitiProcessFlow processFlow = new ActivitiProcessFlow();
		processFlow.setProcessExecutionId(processResponse.getExecutionId());
		processFlow.setProcessDefinitionId(processResponse.getDefinitionId());
		List<String> activeTasks = new ArrayList<String>();
		List<Task> pendingTasks = taskService.createTaskQuery().processInstanceId(processResponse.getExecutionId()).list();
		if(pendingTasks != null) {
			for(Task task: pendingTasks) {
				activeTasks.add(task.getTaskDefinitionKey());
			}
		}
		processFlow.setActiveTasks(activeTasks);
		return processFlow;
	}
	
	@Override
	public ProcessResponse startStatlessBusinessProcess(Param<?> param, String processId) {
		ProcessEngineContext context = new ProcessEngineContext(param);
		Map<String, Object> executionVariables = new HashMap<String, Object>();
		executionVariables.put(Constants.KEY_EXECUTE_PROCESS_CTX.code, context);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(processId, executionVariables);
		ProcessResponse response = new ProcessResponse();
		response.setResponse(context.getOutput());
		response.setExecutionId(pi.getId());
		response.setDefinitionId(pi.getProcessDefinitionId());
		return response;
	}
	
	@Override
	public Object continueBusinessProcessExecution(Param<?> param, String processExecutionId) {
		DeploymentManager deploymentManager = processEngineConfiguration.getDeploymentManager();
		ActivitiProcessFlow processFlow = (ActivitiProcessFlow)((ExecutionEntity<?,?>)param.getRootExecution().getState()).getFlow();
		List<String> activeTasks = processFlow.getActiveTasks();
		ProcessEngineContext context = new ProcessEngineContext(param);
		Map<String, Object> executionVariables = new HashMap<String, Object>();
		executionVariables.put(Constants.KEY_EXECUTE_PROCESS_CTX.code, context);		
		for(String task: activeTasks){
			UserTask userTask = (UserTask)deploymentManager.getProcessDefinitionCache().get(processFlow.getProcessDefinitionId()).getProcess().getFlowElementMap().get(task);
			if(canComplete(param,userTask)) {
				List<Task> activeTaskInstances = taskService.createTaskQuery().processInstanceId(processExecutionId).taskDefinitionKey(task).list();
				for(Task activeTaskIntance: activeTaskInstances)
					taskService.complete(activeTaskIntance.getId(),executionVariables);
			}
		}
		return context.getOutput();
	}
	
	private boolean canComplete(Param<?> param, UserTask userTask) {
		String taskExitCondition = getTaskExitExpression(userTask,"exitCondition");
		if(taskExitCondition != null) {
			return (Boolean)expressionEvaluator.getValue(taskExitCondition, param);
		}
		return true;
	}
	
	private String getTaskExitExpression(UserTask userTask,String extensionName) {
		String taskExitCondition = userTask.getSkipExpression();
		if(taskExitCondition != null)
			return taskExitCondition;
		Map<String, List<ExtensionElement>> extensionElements = userTask.getExtensionElements();
		if (extensionElements == null)
			return null;
		List<ExtensionElement> extensionElementList = extensionElements.get(extensionName);
		if (CollectionUtils.isEmpty(extensionElementList))
			return null;
		ExtensionElement extensionElement = extensionElementList.get(0);
		return extensionElement.getElementText();
	}	

}
