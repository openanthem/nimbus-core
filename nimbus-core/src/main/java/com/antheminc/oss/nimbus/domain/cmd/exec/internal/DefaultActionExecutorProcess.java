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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.bpm.BPMGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.AbstractCommandExecutor;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Jayant Chaudhuri
 *
 */
@EnableLoggingInterceptor
@Getter(value=AccessLevel.PROTECTED)
public class DefaultActionExecutorProcess<R> extends AbstractCommandExecutor<R> {
	
	private BPMGateway bpmGateway;
	
	public DefaultActionExecutorProcess(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		this.bpmGateway = beanResolver.get(BPMGateway.class);
	}
	
	@Override
	protected Output<R> executeInternal(Input input) {
		R response = continueBusinessProcessExecution(input.getContext());
		return Output.instantiate(input, input.getContext(), response);
	}
	
	@SuppressWarnings("unchecked")
	private R continueBusinessProcessExecution(ExecutionContext eCtx){
		QuadModel<?,?> quadModel = getQuadModel(eCtx);
		String processExecutionId = quadModel.getFlow().getProcessExecutionId();
		return (R)getBpmGateway().continueBusinessProcessExecution(quadModel.getView().getAssociatedParam(), processExecutionId);
	}

}
