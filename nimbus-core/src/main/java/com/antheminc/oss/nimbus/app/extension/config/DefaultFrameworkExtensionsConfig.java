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
package com.antheminc.oss.nimbus.app.extension.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationScope;
import com.antheminc.oss.nimbus.domain.model.config.extension.LabelConfigEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.AccessConditionalStateEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.ActivateConditionalStateEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.AuditStateChangeHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.ConfigConditionalStateChangeHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.DobToAgeConverter;
import com.antheminc.oss.nimbus.domain.model.state.extension.EnableConditionalStateEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.ExpressionConditionalStateEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.MessageConditionalHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.ModalStateEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.ParamContextStateEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.RuleStateEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.StaticCodeValueBasedCodeToLabelConverter;
import com.antheminc.oss.nimbus.domain.model.state.extension.ValidateConditionalStateEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.ValidateConditionalStateEventHandler.ValidationAssignmentStrategy;
import com.antheminc.oss.nimbus.domain.model.state.extension.ValuesConditionalOnStateChangeEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.ValuesConditionalOnStateLoadEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.VisibleConditionalStateEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional.SiblingNestedValidationAssignmentStrategy;
import com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional.SiblingValidationAssignmentStrategy;
import com.antheminc.oss.nimbus.domain.model.state.internal.IdParamConverter;

/**
 * @author Soham Chakravarti
 *
 */
@Configuration
public class DefaultFrameworkExtensionsConfig {
	
	@Bean
	public LabelConfigEventHandler labelConfigEventHandler() {
		return new LabelConfigEventHandler();
	}
	
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
	
	@Bean
	public VisibleConditionalStateEventHandler extensionVisibleCondiationalStateEventHandler(BeanResolverStrategy beanResolver) {
		return new VisibleConditionalStateEventHandler(beanResolver);
	}
	
	@Bean
	public ValidateConditionalStateEventHandler extensionValidateConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		return new ValidateConditionalStateEventHandler(beanResolver, null);
	}
	
	@Bean
	public MessageConditionalHandler extensionMessageConditionalHandler(BeanResolverStrategy beanResolver) {
		return new MessageConditionalHandler(beanResolver);
	}
	
	@Bean
	public Map<ValidationScope, ValidationAssignmentStrategy> extensionValidateConditionalAssignmentStrategies() {
		Map<ValidationScope, ValidationAssignmentStrategy> validationAssignmentStrategies = new HashMap<>();
		validationAssignmentStrategies.put(ValidationScope.SIBLING, new SiblingValidationAssignmentStrategy());
		validationAssignmentStrategies.put(ValidationScope.SIBLING_NESTED, new SiblingNestedValidationAssignmentStrategy());
		return validationAssignmentStrategies;
	}
	
	@Bean
	public ExpressionConditionalStateEventHandler expressionConditionalHandler(BeanResolverStrategy beanResolver) {
		return new ExpressionConditionalStateEventHandler(beanResolver);
	}
	
	@Bean
	public IdParamConverter idParamConverter(){
		return new IdParamConverter();
	}
	
	@Bean
	public DobToAgeConverter dobToAgeConverter(){
		return new DobToAgeConverter();
	}
	
	@Bean
	public StaticCodeValueBasedCodeToLabelConverter staticCodeValueBasedCodeToLabelConverter(BeanResolverStrategy beanResolver) {
		return new StaticCodeValueBasedCodeToLabelConverter(beanResolver);
	}
	
}
