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
import com.antheminc.oss.nimbus.domain.cmd.exec.AbstractCommandExecutor;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;


/**
 * @author Rakesh Patel
 *
 */
@EnableLoggingInterceptor
public class DefaultActionExecutorSearch<R> extends AbstractCommandExecutor<R> {
	
	private FunctionExecutor<?, ?> functionExecutor;
	
	public DefaultActionExecutorSearch(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		this.functionExecutor = beanResolver.find(FunctionExecutor.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Output<R> executeInternal(Input input) {
		return (Output<R>)functionExecutor.execute(input);
	}

}
