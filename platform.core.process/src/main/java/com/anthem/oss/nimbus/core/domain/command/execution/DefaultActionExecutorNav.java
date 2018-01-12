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

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.command.execution.nav.NavigationHandler;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionExecutorNav<T> extends AbstractFunctionCommandExecutor<T,String> {
	
	public DefaultActionExecutorNav(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Output<String> executeInternal(Input input) {
		String pageId = (String)executeFunctionHanlder(input, NavigationHandler.class);
		return Output.instantiate(input, input.getContext(), pageId);		
	}
	
	@Override
	protected <F extends FunctionHandler<?, ?>> F getHandler(CommandMessage commandMessage, Class<F> handlerClass) {
		F handler = super.getHandler(commandMessage, handlerClass);
		if(handler == null){
			handler = super.getHandler("default._nav$execute?fn=default",handlerClass);
		}
		return handler;
	}
	
}