/**
 * 
 */
package com.antheminc.oss.nimbus.core.bpm.activiti;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.antheminc.oss.nimbus.platform.spec.model.process.ProcessEngineContext;
import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.CommandBuilder;
import com.antheminc.oss.nimbus.core.domain.command.CommandMessage;
import com.antheminc.oss.nimbus.core.domain.command.CommandElement.Type;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.core.domain.definition.Constants;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;
import com.antheminc.oss.nimbus.core.domain.model.state.repo.ModelRepositoryFactory;
import com.antheminc.oss.nimbus.core.entity.process.ProcessFlow;

/**
 * @author Rakesh Patel
 *
 */
public class PlatformUserTaskActivityBehavior extends UserTaskActivityBehavior {

	private static final long serialVersionUID = 1L;
	
	public static final String URL = "url";
	
	CommandExecutorGateway commandGateway;
	CommandPathVariableResolver pathVariableResolver;
	BeanResolverStrategy beanResolver;
	ModelRepositoryFactory repositoryFactory;
	DomainConfigBuilder domainConfigBuilder;

	
	public PlatformUserTaskActivityBehavior(UserTask userTask) {
		super(userTask);
	}
	
	public void setBeanResolver(BeanResolverStrategy beanResolver){
		this.beanResolver = beanResolver;
	}

	@Override
	public void trigger(DelegateExecution execution, String signalName, Object signalData) {
		super.trigger(execution, signalName, signalData);
	}

	@Override
	public void execute(DelegateExecution execution) {
		super.execute(execution);
		init();
		addActiveTask(execution);
		String url = getExtensionValue(URL);
		if(url == null)
			return;
		String[] commandUrls = url.split("\\r?\\n");
		
		ProcessEngineContext context = getProcessEngineContext(execution);
		MultiOutput output = null;
		for(String commandUrl: commandUrls){
			commandUrl = resolveCommandUrl(execution,commandUrl, context);
			if(StringUtils.isEmpty(commandUrl))
				continue;			
			Command command = CommandBuilder.withUri(commandUrl).getCommand();
			CommandMessage commandMessage = new CommandMessage();
			commandMessage.setCommand(command);
			if(output == null){
				output = commandGateway.execute(commandMessage);
				context.setOutput(output);
			}else{
				MultiOutput commandOutput = commandGateway.execute(commandMessage);
				for(Output<?> op: commandOutput.getOutputs()){
					output.template().add(op);
				}
			}
		}
		
	}
	
	@Override
	public void leave(DelegateExecution execution) {
		super.leave(execution);
		removeActiveTask(execution);
	}
	
	private String getExtensionValue(String extensionName) {
		Map<String, List<ExtensionElement>> extensionElements = userTask.getExtensionElements();
		if (extensionElements == null)
			return null;
		List<ExtensionElement> extensionElementList = extensionElements.get(extensionName);
		if (CollectionUtils.isEmpty(extensionElementList))
			return null;
		ExtensionElement extensionElement = extensionElementList.get(0);
		return extensionElement.getElementText();
	}

	private String resolveCommandUrl(DelegateExecution execution, String commandUrl, ProcessEngineContext context){
		commandUrl = pathVariableResolver.resolve(context.getParam(), commandUrl);
		commandUrl = context.getParam().getRootExecution().getRootCommand().getRelativeUri(commandUrl);
    	return commandUrl;
	}	
	
	private void init(){
		if(this.commandGateway == null){
			this.commandGateway = beanResolver.find(CommandExecutorGateway.class);
			this.pathVariableResolver = beanResolver.find(CommandPathVariableResolver.class);
			this.repositoryFactory = beanResolver.find(ModelRepositoryFactory.class);
			this.domainConfigBuilder = beanResolver.find(DomainConfigBuilder.class);

		}
	}
	
	private ProcessEngineContext getProcessEngineContext(DelegateExecution execution) {
		return (ProcessEngineContext)execution.getVariable(Constants.KEY_EXECUTE_PROCESS_CTX.code);
	}
	
	private void addActiveTask(DelegateExecution execution) {
		ProcessEngineContext context = getProcessEngineContext(execution);
		ActivitiProcessFlow processFlow = (ActivitiProcessFlow)((ExecutionEntity<?,?>)context.getParam().getRootExecution().getState()).getFlow();
		if(processFlow == null)
			return;
		List<String> activeTasks = processFlow.getActiveTasks();
		if(activeTasks == null)
			return;
		activeTasks = new ArrayList<String>(activeTasks);
		activeTasks.add(userTask.getId());
		processFlow.setActiveTasks(activeTasks);	
		updateProcessState(context,activeTasks);
	}
	
	private void removeActiveTask(DelegateExecution execution) {
		ProcessEngineContext context = getProcessEngineContext(execution);
		ActivitiProcessFlow processFlow = (ActivitiProcessFlow)((ExecutionEntity<?,?>)context.getParam().getRootExecution().getState()).getFlow();
		if(processFlow == null)
			return;
		List<String> activeTasks = processFlow.getActiveTasks();
		if(activeTasks == null)
			return;
		activeTasks = new ArrayList<String>(activeTasks);
		activeTasks.remove(userTask.getId());
		processFlow.setActiveTasks(activeTasks);
		updateProcessState(context,activeTasks);
	}	
	
	protected void updateProcessState(ProcessEngineContext context, List<String> activeTasks) {
		ModelConfig<?> modelConfig = domainConfigBuilder.getModel(ProcessFlow.class);
		Repo repo = modelConfig.getRepo();
		String processStateAlias = StringUtils.isBlank(repo.alias()) ? modelConfig.getAlias() : repo.alias();
		String entityProcessAlias = context.getParam().getRootDomain().getConfig().getAlias() + "_" + processStateAlias;
		String entityRefId = context.getParam().getRootExecution().getRootCommand().getRefId(Type.DomainAlias);
		repositoryFactory.get(repo)._update(entityProcessAlias, entityRefId, "/activeTasks", activeTasks);
	}	

}
