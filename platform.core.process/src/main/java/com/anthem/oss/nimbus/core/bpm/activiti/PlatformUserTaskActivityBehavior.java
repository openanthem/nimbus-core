/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm.activiti;

import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.anthem.nimbus.platform.spec.model.process.ProcessEngineContext;
import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandPathVariableResolver;
import com.anthem.oss.nimbus.core.domain.definition.Constants;

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
	
	public PlatformUserTaskActivityBehavior(UserTask userTask) {
		super(userTask);
//		this.commandGateway = beanResolver.find(CommandExecutorGateway.class);
//		this.pathVariableResolver = beanResolver.find(CommandPathVariableResolver.class);
//		this.activitiExpressionManager = beanResolver.find(ActivitiExpressionManager.class);
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
		String url = getExtensionValue(URL);
		if(url == null)
			return;
		String[] commandUrls = url.split("\\r?\\n");
		
		ProcessEngineContext context = (ProcessEngineContext)execution.getVariable(Constants.KEY_EXECUTE_PROCESS_CTX.code);
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
		commandUrl = pathVariableResolver.resolve(context.getActionParam(), commandUrl);
		commandUrl = context.getExecutionContext().getCommandMessage().getCommand().getRelativeUri(commandUrl);
    	return commandUrl;
	}	
	
	private void init(){
		if(this.commandGateway == null){
			this.commandGateway = beanResolver.find(CommandExecutorGateway.class);
			this.pathVariableResolver = beanResolver.find(CommandPathVariableResolver.class);
		}
	}

}
