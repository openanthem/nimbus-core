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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal.process;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.bpm.BPMGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.AbstractFunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.ProcessResponse;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Jayant Chaudhuri
 *
 */
@EnableLoggingInterceptor
@Getter(value=AccessLevel.PROTECTED)
public class StatelessBPMFunctionHanlder<T,R> extends AbstractFunctionHandler<T, R> {
	
	private BPMGateway bpmGateway;
	
	public StatelessBPMFunctionHanlder(BeanResolverStrategy beanResolver) {
		this.bpmGateway = beanResolver.find(BPMGateway.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public R execute(ExecutionContext executionContext, Param<T> actionParameter) {
		String processId = executionContext.getCommandMessage().getCommand().getFirstParameterValue(Constants.KEY_EXECUTE_PROCESS_ID.code);
		ProcessResponse response = getBpmGateway().startStatlessBusinessProcess(executionContext.getQuadModel().getView().getAssociatedParam(), processId);
		return (R)response.getResponse();
	}
}
