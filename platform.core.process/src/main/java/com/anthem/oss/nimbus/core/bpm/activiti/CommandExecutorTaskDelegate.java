package com.anthem.oss.nimbus.core.bpm.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;

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
 * 
 * @author Jayant Chaudhuri
 *
 */
public class CommandExecutorTaskDelegate implements JavaDelegate{
	
	CommandExecutorGateway commandGateway;
	CommandPathVariableResolver pathVariableResolver;
	ActivitiExpressionManager activitiExpressionManager;
	
	private Expression url;

	public CommandExecutorTaskDelegate(BeanResolverStrategy beanResolver) {
		this.commandGateway = beanResolver.find(CommandExecutorGateway.class);
		this.pathVariableResolver = beanResolver.find(CommandPathVariableResolver.class);
		this.activitiExpressionManager = beanResolver.find(ActivitiExpressionManager.class);
	}
	
	@Override
	public void execute(DelegateExecution execution) {
		String[] commandUrls = url.getExpressionText().split("\\r?\\n");
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
	
	
	private String resolveCommandUrl(DelegateExecution execution, String commandUrl, ProcessEngineContext context){
		//Expression expression = activitiExpressionManager.createExpression(commandUrl);
		//Object value = expression.getValue(execution);
		//if(value == null)
		//	return null;
		//commandUrl = (String)value;
		commandUrl = pathVariableResolver.resolve(context.getActionParam(), commandUrl);
		commandUrl = context.getExecutionContext().getCommandMessage().getCommand().getRelativeUri(commandUrl);
    	return commandUrl;
	}
	
	
}
