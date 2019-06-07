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
package com.antheminc.oss.nimbus.domain.cmd.exec;

import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.domain.cmd.CommandMessageConverter;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;

import lombok.Data;

/**
 * @author Rakesh Patel
 *
 */
@Data
public abstract class AbstractFunctionHandler<T, R> implements FunctionHandler<T, R> {

	@Autowired DomainConfigBuilder domainConfigBuilder;
	
	@Autowired ModelRepositoryFactory repFactory;
	
	@Autowired CommandMessageConverter converter;
	
	@Autowired CommandPathVariableResolver pathVariableResolver;
	
	
	protected ModelConfig<?> getRootDomainConfig(ExecutionContext eCtx) {
		return getDomainConfigBuilder().getRootDomainOrThrowEx(eCtx.getCommandMessage().getCommand().getRootDomainAlias());
	}
	
}
