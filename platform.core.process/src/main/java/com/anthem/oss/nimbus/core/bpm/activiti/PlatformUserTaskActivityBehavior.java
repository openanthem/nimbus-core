/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm.activiti;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.anthem.nimbus.platform.spec.model.process.ProcessEngineContext;
import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandPathVariableResolver;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.utils.ProcessBeanResolver;

import io.swagger.models.AbstractModel;

/**
 * @author Rakesh Patel
 *
 */
public class PlatformUserTaskActivityBehavior extends UserTaskActivityBehavior {

	private static final long serialVersionUID = 1L;
	
	public static final String URL = "url";
	
	CommandExecutorGateway commandGateway;
	CommandPathVariableResolver pathVariableResolver;
	ActivitiExpressionManager activitiExpressionManager;
	
	public PlatformUserTaskActivityBehavior(BeanResolverStrategy beanResolver, UserTask userTask) {
		super(userTask);
		this.commandGateway = beanResolver.find(CommandExecutorGateway.class);
		this.pathVariableResolver = beanResolver.find(CommandPathVariableResolver.class);
		this.activitiExpressionManager = beanResolver.find(ActivitiExpressionManager.class);
		
	}

	@Override
	public void trigger(DelegateExecution execution, String signalName, Object signalData) {
		super.trigger(execution, signalName, signalData);
	}

	@Override
	public void execute(DelegateExecution execution) {
		super.execute(execution);
		String url = getExtensionValue(URL);
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
//		Expression expression = activitiExpressionManager.createExpression(commandUrl);
//		Object value = expression.getValue(execution);
//		if(value == null)
//			return null;
//		commandUrl = (String)value;
		commandUrl = pathVariableResolver.resolve(context.getActionParam(), commandUrl);
		commandUrl = context.getExecutionContext().getCommandMessage().getCommand().getRelativeUri(commandUrl);
    	return commandUrl;
	}	

}
