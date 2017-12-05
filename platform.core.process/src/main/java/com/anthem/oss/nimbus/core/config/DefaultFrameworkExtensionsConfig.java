/**
 * 
 */
package com.antheminc.oss.nimbus.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.model.state.extension.AccessConditionalStateEventHandler;
import com.antheminc.oss.nimbus.core.domain.model.state.extension.ActivateConditionalStateEventHandler;
import com.antheminc.oss.nimbus.core.domain.model.state.extension.AuditStateChangeHandler;
import com.antheminc.oss.nimbus.core.domain.model.state.extension.ConfigConditionalStateChangeHandler;
import com.antheminc.oss.nimbus.core.domain.model.state.extension.EnableConditionalStateEventHandler;
import com.antheminc.oss.nimbus.core.domain.model.state.extension.ModalStateEventHandler;
import com.antheminc.oss.nimbus.core.domain.model.state.extension.ParamContextStateEventHandler;
import com.antheminc.oss.nimbus.core.domain.model.state.extension.RuleStateEventHandler;
import com.antheminc.oss.nimbus.core.domain.model.state.extension.ValuesConditionalOnStateChangeEventHandler;
import com.antheminc.oss.nimbus.core.domain.model.state.extension.ValuesConditionalOnStateLoadEventHandler;

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
