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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.AbstractCommandExecutor;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.model.config.EntityConfig;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

/**
 * @author Soham Chakravarti
 *
 */
@EnableLoggingInterceptor
public class DefaultActionExecutorConfig extends AbstractCommandExecutor<EntityConfig<?>> {
	
	public DefaultActionExecutorConfig(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	/**
	 * Returns {@linkplain ModelConfig} for domain root call, otherwise, would return {@linkplain ParamConfig} for nested domain command path
	 */
	@Override
	protected Output<EntityConfig<?>> executeInternal(Input input) {
		Param<?> p = findParamByCommandOrThrowEx(input.getContext());

		return Output.instantiate(input, input.getContext(), p.getConfig());
	}
} 