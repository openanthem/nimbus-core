/**
 *  Copyright 2016-2019 the original author or authors.
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

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.bpm.ProcessEngineContext;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.support.EnableAPIMetricCollection;
import com.antheminc.oss.nimbus.support.EnableAPIMetricCollection.LogLevel;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Jayant Chaudhuri
 *
 */

@Getter 
@EnableAPIMetricCollection(args = LogLevel.info)
public class CommandExecutorTaskDelegate implements JavaDelegate{
	
	CommandExecutorGateway commandGateway;
	CommandPathVariableResolver pathVariableResolver;
	ActivitiExpressionManager activitiExpressionManager;
	
	@Setter
	private Expression url;

	public CommandExecutorTaskDelegate(BeanResolverStrategy beanResolver) {
		this.commandGateway = beanResolver.find(CommandExecutorGateway.class);
		this.pathVariableResolver = beanResolver.find(CommandPathVariableResolver.class);
		this.activitiExpressionManager = beanResolver.find(ActivitiExpressionManager.class);
	}
	
	@Override
	public void execute(DelegateExecution execution) {
		String[] commandUrls = getUrl().getExpressionText().split("\\r?\\n");
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
				output = getCommandGateway().execute(commandMessage);
				context.setOutput(output);
			}else{
				MultiOutput commandOutput = getCommandGateway().execute(commandMessage);
				for(Output<?> op: commandOutput.getOutputs()){
					output.template().add(op);
				}
			}
		}
	}
	
	
	private String resolveCommandUrl(ProcessEngineContext context, String commandUrl){
		commandUrl = getPathVariableResolver().resolve(context.getParam(), commandUrl);
		commandUrl = context.getParam().getRootExecution().getRootCommand().getRelativeUri(commandUrl);
    	return commandUrl;
	}
	
	
}
