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
			commandUrl = resolveCommandUrl(context,commandUrl);
			if(StringUtils.isEmpty(commandUrl))
				continue;			
			Command command = CommandBuilder.withUri(commandUrl).getCommand();
			CommandMessage commandMessage = new CommandMessage(command,null);
			//commandMessage.setCommand(command);
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
	
	
	private String resolveCommandUrl(ProcessEngineContext context, String commandUrl){
		commandUrl = pathVariableResolver.resolve(context.getParam(), commandUrl);
		commandUrl = context.getParam().getRootExecution().getRootCommand().getRelativeUri(commandUrl);
    	return commandUrl;
	}
	
	
}
