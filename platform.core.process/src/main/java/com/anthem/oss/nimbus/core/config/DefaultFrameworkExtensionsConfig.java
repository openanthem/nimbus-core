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
package com.anthem.oss.nimbus.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.model.state.extension.AccessConditionalStateEventHandler;
import com.anthem.oss.nimbus.core.domain.model.state.extension.ActivateConditionalStateEventHandler;
import com.anthem.oss.nimbus.core.domain.model.state.extension.AuditStateChangeHandler;
import com.anthem.oss.nimbus.core.domain.model.state.extension.ConfigConditionalStateChangeHandler;
import com.anthem.oss.nimbus.core.domain.model.state.extension.EnableConditionalStateEventHandler;
import com.anthem.oss.nimbus.core.domain.model.state.extension.ModalStateEventHandler;
import com.anthem.oss.nimbus.core.domain.model.state.extension.ParamContextStateEventHandler;
import com.anthem.oss.nimbus.core.domain.model.state.extension.RuleStateEventHandler;
import com.anthem.oss.nimbus.core.domain.model.state.extension.ValuesConditionalOnStateChangeEventHandler;
import com.anthem.oss.nimbus.core.domain.model.state.extension.ValuesConditionalOnStateLoadEventHandler;

/**
 * @author Soham Chakravarti
 *
 */
@Configuration
public class DefaultFrameworkExtensionsConfig {
	
	@Bean
	public ModalStateEventHandler extensionModalStateEventHandler() {
		return new ModalStateEventHandler();
	}
	
	@Bean
	public ParamContextStateEventHandler extensionParamContextStateEventHandler() {
		return new ParamContextStateEventHandler();
	}
	
	@Bean
	public RuleStateEventHandler extensionRuleStateEventHandler(BeanResolverStrategy beanResolver) {
		return new RuleStateEventHandler(beanResolver);
	}
	
	@Bean
	public ValuesConditionalOnStateLoadEventHandler extensionValuesConditionalOnStateLoadHandler(BeanResolverStrategy beanResolver) {
		return new ValuesConditionalOnStateLoadEventHandler(beanResolver);
	}
	
	@Bean
	public ValuesConditionalOnStateChangeEventHandler extensionValuesConditionalOnStateChangeHandler(BeanResolverStrategy beanResolver) {
		return new ValuesConditionalOnStateChangeEventHandler(beanResolver);
	}
	
	@Bean
	public ActivateConditionalStateEventHandler extensionActivateConditionalHandler(BeanResolverStrategy beanResolver) {
		return new ActivateConditionalStateEventHandler(beanResolver);
	}

	@Bean
	public AuditStateChangeHandler extensionAuditStateChangeHandler(BeanResolverStrategy beanResolver) {
		return new AuditStateChangeHandler(beanResolver);
	}
	
	@Bean
	public AccessConditionalStateEventHandler extensionAccessConditionalHandler(BeanResolverStrategy beanResolver) {
		return new AccessConditionalStateEventHandler(beanResolver);
	}

	@Bean
	public ConfigConditionalStateChangeHandler extensionConfigConditionalStateChangeHandler(BeanResolverStrategy beanResolver) {
		return new ConfigConditionalStateChangeHandler(beanResolver);
	}
	
	@Bean
	public EnableConditionalStateEventHandler extensionEnableCondiationalStateEventHandler(BeanResolverStrategy beanResolver) {
		return new EnableConditionalStateEventHandler(beanResolver);
	}
}
