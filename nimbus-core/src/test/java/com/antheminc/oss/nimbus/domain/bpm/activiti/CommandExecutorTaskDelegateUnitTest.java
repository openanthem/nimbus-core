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

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.bpm.ProcessEngineContext;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * @author Tony Lopez
 *
 */
public class CommandExecutorTaskDelegateUnitTest {

	private CommandExecutorTaskDelegate testee;
	private BeanResolverStrategy beanResolver;
	private CommandExecutorGateway commandGateway;
	private CommandPathVariableResolver pathVariableResolver;
	private ActivitiExpressionManager activitiExpressionManager;
	private Expression expression;
	
	@Before
	public void init() {
		this.beanResolver = Mockito.mock(BeanResolverStrategy.class);
		this.commandGateway = Mockito.mock(CommandExecutorGateway.class);
		this.pathVariableResolver = Mockito.mock(CommandPathVariableResolver.class);
		this.activitiExpressionManager = Mockito.mock(ActivitiExpressionManager.class);
		this.expression = Mockito.mock(Expression.class);
		
		Mockito.when(this.beanResolver.find(CommandExecutorGateway.class)).thenReturn(this.commandGateway);
		Mockito.when(this.beanResolver.find(CommandPathVariableResolver.class)).thenReturn(this.pathVariableResolver);
		Mockito.when(this.beanResolver.find(ActivitiExpressionManager.class)).thenReturn(this.activitiExpressionManager);
		
		this.testee = new CommandExecutorTaskDelegate(this.beanResolver);
		this.testee.setUrl(this.expression);
	}
	
	@Test
	public void testExecuteSingleOutput() {
		String expressionText = "/app/org/client/p/<!/.m/entity!>_view/p1";
		String[] commandUrls = expressionText.split("\\r?\\n");
		String resolvedCommandUrl0 = "/app/org/client/p/sample_view/p1";
		MultiOutput expectedOutput0 = new MultiOutput(resolvedCommandUrl0, getMockExecutionContext(), Action._get, Behavior.$execute);
		
		ProcessEngineContext context = Mockito.mock(ProcessEngineContext.class);
		Mockito.when(this.expression.getExpressionText()).thenReturn(expressionText);
		DelegateExecution execution = Mockito.mock(DelegateExecution.class);
		
		Mockito.when(execution.getVariable(Constants.KEY_EXECUTE_PROCESS_CTX.code)).thenReturn(context);
		mockRelativeUriRetrieval(context, resolvedCommandUrl0, resolvedCommandUrl0);
		Mockito.when(this.pathVariableResolver.resolve(context.getParam(), commandUrls[0])).thenReturn(resolvedCommandUrl0);
		Mockito.when(this.commandGateway.execute(Mockito.isA(CommandMessage.class))).thenReturn(expectedOutput0);
		
		this.testee.execute(execution);
		
		Mockito.verify(context, Mockito.times(1)).setOutput(Mockito.isA(MultiOutput.class));
	}
	
	@Test
	public void testExecuteEmptyCommandURL() {
		String expressionText = "";
		String[] commandUrls = expressionText.split("\\r?\\n");
		String resolvedCommandUrl0 = "";
		MultiOutput expectedOutput0 = new MultiOutput(resolvedCommandUrl0, getMockExecutionContext(), Action._get, Behavior.$execute);
		
		ProcessEngineContext context = Mockito.mock(ProcessEngineContext.class);
		Mockito.when(this.expression.getExpressionText()).thenReturn(expressionText);
		DelegateExecution execution = Mockito.mock(DelegateExecution.class);
		
		Mockito.when(execution.getVariable(Constants.KEY_EXECUTE_PROCESS_CTX.code)).thenReturn(context);
		mockRelativeUriRetrieval(context, resolvedCommandUrl0, resolvedCommandUrl0);
		Mockito.when(this.pathVariableResolver.resolve(context.getParam(), commandUrls[0])).thenReturn(resolvedCommandUrl0);
		Mockito.when(this.commandGateway.execute(Mockito.isA(CommandMessage.class))).thenReturn(expectedOutput0);
		
		this.testee.execute(execution);
		
		Mockito.verify(context, Mockito.times(0)).setOutput(Mockito.isA(MultiOutput.class));
	}
	
	@Test
	public void testExecuteMultipleOutputs() {
		// TODO
	}
	
	private void mockRelativeUriRetrieval(ProcessEngineContext context, String relativeUrisToRetrieve, String relativeUrisToReturn) {
		this.mockRelativeUriRetrieval(context, new String[] { relativeUrisToRetrieve },  new String[] { relativeUrisToReturn });
	}
	
	private void mockRelativeUriRetrieval(ProcessEngineContext context, String[] relativeUrisToRetrieve, String[] relativeUrisToReturn) {
		Param param = Mockito.mock(Param.class);
		ExecutionModel executionModel = Mockito.mock(ExecutionModel.class);
		Command rootCmd = Mockito.mock(Command.class);
		Mockito.when(context.getParam()).thenReturn(param);
		Mockito.when(param.getRootExecution()).thenReturn(executionModel);
		Mockito.when(executionModel.getRootCommand()).thenReturn(rootCmd);
		for (int i = 0; i < relativeUrisToRetrieve.length; i++) {
			String relativeUriToRetrieve = relativeUrisToRetrieve[i];
			String relativeUriToReturn = relativeUrisToReturn[i];
			Mockito.when(rootCmd.getRelativeUri(relativeUriToRetrieve)).thenReturn(relativeUriToReturn);
		}
	}
	
	private ExecutionContext getMockExecutionContext() {
		CommandMessage cmdMsg = Mockito.mock(CommandMessage.class);
		Command cmd = Mockito.mock(Command.class);
		CommandElement rootDomainElement = Mockito.mock(CommandElement.class);
		Mockito.when(cmdMsg.getCommand()).thenReturn(cmd);
		Mockito.when(cmd.getRootDomainElement()).thenReturn(rootDomainElement);
		Mockito.when(rootDomainElement.getRefId()).thenReturn(1L);
		return new ExecutionContext(cmdMsg);
	}
}
