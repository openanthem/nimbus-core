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
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.app.extension.config.ActivitiProcessDefinitionCache;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.bpm.BPMGateway;
import com.antheminc.oss.nimbus.domain.bpm.ProcessEngineContext;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.cmd.exec.ProcessResponse;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.internal.ExecutionEntity;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;
import com.antheminc.oss.nimbus.entity.process.ProcessFlow;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.antheminc.oss.nimbus.support.expr.ExpressionEvaluator;

import lombok.Getter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter
@EnableLoggingInterceptor
public class ActivitiBPMGateway implements BPMGateway {
	
	protected final JustLogit logit = new JustLogit(this.getClass());
	public static final String EVALURLS = "evalURLs";
	
	private RuntimeService runtimeService;
	private TaskService taskService;
	private SpringProcessEngineConfiguration processEngineConfiguration;
	private ExpressionEvaluator expressionEvaluator;
	private Boolean supportStatefulProcesses;
	private ModelRepositoryFactory repositoryFactory;
	private DomainConfigBuilder domainConfigBuilder;
	private CommandExecutorGateway commandGateway;
	private CommandPathVariableResolver pathVariableResolver;
	
	public ActivitiBPMGateway (BeanResolverStrategy beanResolver,Boolean supportStatefulProcesses) {
		this.expressionEvaluator = beanResolver.find(ExpressionEvaluator.class);
		this.runtimeService = beanResolver.find(RuntimeService.class);
		this.taskService = beanResolver.find(TaskService.class);
		this.processEngineConfiguration = beanResolver.find(SpringProcessEngineConfiguration.class);
		this.supportStatefulProcesses = supportStatefulProcesses;
		this.repositoryFactory = beanResolver.find(ModelRepositoryFactory.class);
		this.domainConfigBuilder = beanResolver.find(DomainConfigBuilder.class);	
		this.commandGateway = beanResolver.find(CommandExecutorGateway.class);
		this.pathVariableResolver = beanResolver.find(CommandPathVariableResolver.class);
	}
	
	@Override
	public ActivitiProcessFlow startBusinessProcess(Param<?> param, String processId) {
		if(!getSupportStatefulProcesses())
			return null;
		ProcessResponse processResponse = startStatlessBusinessProcess(param, processId);
		ActivitiProcessFlow processFlow = new ActivitiProcessFlow();
		processFlow.setProcessExecutionId(processResponse.getExecutionId());
		processFlow.setProcessDefinitionId(processResponse.getDefinitionId());
		List<String> activeTasks = new ArrayList<String>();
		List<Task> pendingTasks = getTaskService().createTaskQuery().processInstanceId(processResponse.getExecutionId()).list();
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
		ProcessInstance pi = getRuntimeService().startProcessInstanceByKey(processId, executionVariables);
		ProcessResponse response = new ProcessResponse();
		response.setResponse(context.getOutput());
		response.setExecutionId(pi.getId());
		response.setDefinitionId(pi.getProcessDefinitionId());
		return response;
	}
	
	@Override
	public Object continueBusinessProcessExecution(Param<?> param, String processExecutionId) {
		ActivitiProcessFlow processFlow = (ActivitiProcessFlow)((ExecutionEntity<?,?>)param.getRootExecution().getState()).getFlow();
		List<String> activeTasks = processFlow.getActiveTasks();
		ProcessEngineContext context = new ProcessEngineContext(param);
		Map<String, Object> executionVariables = new HashMap<String, Object>();
		executionVariables.put(Constants.KEY_EXECUTE_PROCESS_CTX.code, context);		
		for(String task: activeTasks){
			evaulateAndExecuteTask(param,processExecutionId,task);
		}
		return context.getOutput();
	}
	
	private void evaulateAndExecuteTask(Param<?> param, String processExecutionId, String task) {
		DeploymentManager deploymentManager = getProcessEngineConfiguration().getDeploymentManager();
		ActivitiProcessFlow processFlow = (ActivitiProcessFlow)((ExecutionEntity<?,?>)param.getRootExecution().getState()).getFlow();
		ActivitiProcessDefinitionCache cache = (ActivitiProcessDefinitionCache)deploymentManager.getProcessDefinitionCache();
		refreshProcessDefinitionCacheIfApplicable(processFlow.getProcessDefinitionId(), cache, param);
		if(cache.size() == 0) {
			logit.error(() -> "Could not get ProcessDefinitionCache from either processEngineConfiguration or db query findDeployedLatestProcessDefinitionByKey (which should refresh the cache) while executing param "
							+ param + " and process Execution Id: " + processExecutionId);
		}
		
		UserTask userTask = (UserTask)cache.findByKey(getProcessKeyFromDefinitionId(processFlow.getProcessDefinitionId(), param)).getProcess().getFlowElementMap().get(task);
		if(canComplete(param,userTask))
			executeTask(param,processExecutionId,task);
	}
	
	private void executeTask(Param<?> param, String processExecutionId, String task) {
		List<Task> activeTaskInstances = getTaskService().createTaskQuery().processInstanceId(processExecutionId).taskDefinitionKey(task).list();
		for(Task activeTaskIntance: activeTaskInstances) {
			try {
				ProcessEngineContext context = new ProcessEngineContext(param);
				Map<String, Object> executionVariables = new HashMap<String, Object>();
				executionVariables.put(Constants.KEY_EXECUTE_PROCESS_CTX.code, context);						
				getTaskService().complete(activeTaskIntance.getId(),executionVariables);
			}catch (Exception e) {
				throw new FrameworkRuntimeException("Error executing bpm flow with excecution id:"+processExecutionId+" when attempting to complete the task:"+task,e);
			}finally {
				updateProcessState(param,processExecutionId);
			}
		}		
	}

	private void updateProcessState(Param<?> param, String processExecutionId) {
		ActivitiProcessFlow processFlow = (ActivitiProcessFlow)((ExecutionEntity<?,?>)param.getRootExecution().getState()).getFlow();
		List<String> activeTasks = new ArrayList<String>();
		List<Task> activeTasksFromDB = getTaskService().createTaskQuery().processInstanceId(processExecutionId).list();
		activeTasksFromDB.iterator().forEachRemaining( t -> activeTasks.add(t.getTaskDefinitionKey()));
		ModelConfig<?> modelConfig = domainConfigBuilder.getModel(ProcessFlow.class);
		Repo repo = modelConfig.getRepo();
		String processStateAlias = StringUtils.isBlank(repo.alias()) ? modelConfig.getAlias() : repo.alias();
		String entityProcessAlias = param.getRootDomain().getConfig().getAlias() + "_" + processStateAlias;
		Long entityRefId = param.getRootExecution().getRootCommand().getRefId(Type.DomainAlias);
		repositoryFactory.get(repo)._update(entityProcessAlias, entityRefId, "/activeTasks", activeTasks);
		processFlow.setActiveTasks(activeTasks);
	}	
	
	private boolean canComplete(Param<?> param, UserTask userTask) {
		String taskExitUrl = getExtensionElementExpression(userTask, EVALURLS);
		if(StringUtils.isNotEmpty(taskExitUrl)) {
			executeExitUrls(taskExitUrl, param);
		}
		String taskExitCondition = getTaskExitExpression(userTask,"exitCondition");
		if(taskExitCondition != null) {
			return (Boolean)getExpressionEvaluator().getValue(taskExitCondition, param);
		}
		return true;
	}
	
	private void refreshProcessDefinitionCacheIfApplicable(String processDefinitionId, ActivitiProcessDefinitionCache cache, Param<?> param) {
		if(cache.size() == 0) {
			CommandExecutor commandExecutor = getProcessEngineConfiguration().getCommandExecutor();
			if(commandExecutor != null) {
				commandExecutor.execute((commandContext) -> {
				          return Context.getProcessEngineConfiguration()
				                        .getDeploymentManager()
				                        .findDeployedLatestProcessDefinitionByKey(getProcessKeyFromDefinitionId(processDefinitionId, param));
				});
			}
		}
	}
	
	private String getTaskExitExpression(UserTask userTask,String extensionName) {
		String taskExitCondition = userTask.getSkipExpression();
		if(taskExitCondition != null)
			return taskExitCondition;
		else 
			return getExtensionElementExpression(userTask, extensionName);
	}	
	
	private String getExtensionElementExpression(UserTask userTask,String extensionName) {
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

	private void executeExitUrls(String urls, Param<?> param) {
		String[] urlList = StringUtils.split(urls, "\r\n");
		MultiOutput output = null;
		for(String url: urlList) {
			url = resolveCommandUrl(url, param);
			if(StringUtils.isEmpty(url))
				continue;			
			Command command = CommandBuilder.withUri(url).getCommand();
			CommandMessage commandMessage = new CommandMessage();
			commandMessage.setCommand(command);
			if(output == null){
				output = commandGateway.execute(commandMessage);
			}else{
				MultiOutput commandOutput = commandGateway.execute(commandMessage);
				for(Output<?> op: commandOutput.getOutputs()){
					output.template().add(op);
				}
			}
		}
	}
	
	private String resolveCommandUrl(String commandUrl, Param<?> param){
		commandUrl = pathVariableResolver.resolve(param, commandUrl);
		commandUrl = param.getRootExecution().getRootCommand().getRelativeUri(commandUrl);
    	return commandUrl;
	}
}
