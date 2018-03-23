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
package com.antheminc.oss.nimbus.domain.bpm.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.apache.commons.collections.CollectionUtils;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.app.extension.config.ActivitiProcessDefinitionCache;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.bpm.BPMGateway;
import com.antheminc.oss.nimbus.domain.bpm.ProcessEngineContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.ProcessResponse;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.internal.ExecutionEntity;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.antheminc.oss.nimbus.support.expr.ExpressionEvaluator;

/**
 * @author Jayant Chaudhuri
 *
 */
public class ActivitiBPMGateway implements BPMGateway {
	
	protected final JustLogit logit = new JustLogit(this.getClass());
	
	RuntimeService runtimeService;
	
	TaskService taskService;
	
	SpringProcessEngineConfiguration processEngineConfiguration;
	
	private ExpressionEvaluator expressionEvaluator;
	
	private Boolean supportStatefulProcesses;
	
	public ActivitiBPMGateway (BeanResolverStrategy beanResolver,Boolean supportStatefulProcesses) {
		this.expressionEvaluator = beanResolver.find(ExpressionEvaluator.class);
		this.runtimeService = beanResolver.find(RuntimeService.class);
		this.taskService = beanResolver.find(TaskService.class);
		this.processEngineConfiguration = beanResolver.find(SpringProcessEngineConfiguration.class);
		this.supportStatefulProcesses = supportStatefulProcesses;

	}
	
	@Override
	public ActivitiProcessFlow startBusinessProcess(Param<?> param, String processId) {
		if(!supportStatefulProcesses)
			return null;
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
			ActivitiProcessDefinitionCache cache = (ActivitiProcessDefinitionCache)deploymentManager.getProcessDefinitionCache();
			refreshProcessDefinitionCacheIfApplicable(processFlow.getProcessDefinitionId(), cache, param);
			if(cache.size() == 0) {
				logit.error(() -> "Could not get ProcessDefinitionCache from either processEngineConfiguration or db query findDeployedLatestProcessDefinitionByKey (which should refresh the cache) while executing param "
								+ param + " and process Execution Id: " + processExecutionId);
			}
			
			UserTask userTask = (UserTask)cache.findByKey(getProcessKeyFromDefinitionId(processFlow.getProcessDefinitionId(), param)).getProcess().getFlowElementMap().get(task);
			if(canComplete(param,userTask)) {
				List<Task> activeTaskInstances = taskService.createTaskQuery().processInstanceId(processExecutionId).taskDefinitionKey(task).list();
				for(Task activeTaskIntance: activeTaskInstances)
					taskService.complete(activeTaskIntance.getId(),executionVariables);
			}
		}
		return context.getOutput();
	}

	private void refreshProcessDefinitionCacheIfApplicable(String processDefinitionId, ActivitiProcessDefinitionCache cache, Param<?> param) {
		if(cache.size() == 0) {
			CommandExecutor commandExecutor = processEngineConfiguration.getCommandExecutor();
			if(commandExecutor != null) {
				commandExecutor.execute((commandContext) -> {
				          return Context.getProcessEngineConfiguration()
				                        .getDeploymentManager()
				                        .findDeployedLatestProcessDefinitionByKey(getProcessKeyFromDefinitionId(processDefinitionId, param));
				});
			}
		}
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
	
	
	private String getProcessKeyFromDefinitionId(String processDefinitionId, Param<?> param) {
		return Optional.ofNullable(processDefinitionId)
			.map(id -> id.split(":")[0])
			.orElseThrow(() -> new FrameworkRuntimeException("Process Definition Id cannot be null while trying to continue the process for param "+param+" execution"));
		
	}

}
