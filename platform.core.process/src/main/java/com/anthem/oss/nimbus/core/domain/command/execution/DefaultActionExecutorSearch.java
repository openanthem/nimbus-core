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
package com.anthem.oss.nimbus.core.domain.command.execution;


import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;


/**
 * @author Rakesh Patel
 *
 */
public class DefaultActionExecutorSearch<T, R> extends AbstractFunctionCommandExecutor<T, R> {
	
	public DefaultActionExecutorSearch(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Output<R> executeInternal(Input input) {
		R response = (R)executeFunctionHanlder(input, FunctionHandler.class);
		
		String projectPath = input.getContext().getCommandMessage().getCommand().getFirstParameterValue("project");
		// TODO optimize for more than one connection output
		if(StringUtils.isNotBlank(projectPath)){
			Param<R> pRoot = findParamByCommandOrThrowEx(input.getContext());
			if(response instanceof Collection<?>) {
				response = (R) ((Collection<?>) response).iterator().next();
			}
			pRoot.setState(response);
			response = (R)pRoot.findParamByPath(projectPath).getState();
		}
		
		return Output.instantiate(input, input.getContext(), response);
	}

}
