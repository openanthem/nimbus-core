/**
 *
 *  Copyright 2012-2017 the original author or authors.
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.DefaultBeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.process.ParamUpdateEventListener;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.config.builder.DefaultValidatorProvider;
import com.anthem.oss.nimbus.core.domain.model.config.builder.EntityConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.builder.DefaultQuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.builder.EntityStateBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.builder.PageNavigationInitializer;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.util.JustLogit;
import com.anthem.oss.nimbus.core.util.SecurityUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sandeep Mantha
 * @author Soham Chakravarti
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="domain.model")
@Getter @Setter
public class DefaultCoreBuilderConfig {
	
	private Map<String, String> typeClassMappings;
	
	private List<String> basePackages;
	
	@Value("${platform.config.secure.regex}")
	private String secureRegex;
	
	@Bean
	@DependsOn("securityUtils")
	public JustLogit justLogit() {
		return new JustLogit();		
	}
	
	@Bean
	public BeanResolverStrategy defaultBeanResolver(ApplicationContext appCtx) {
		return new DefaultBeanResolverStrategy(appCtx);
	}
	
	@Bean
	public DomainConfigBuilder domainConfigBuilder(EntityConfigBuilder configBuilder){
		return new DomainConfigBuilder(configBuilder, basePackages);
	}
	
//	@Bean
//	public ParamEventAMQPListener paramEventAMQPListener(SimpMessageSendingOperations messageTemplate,CommandTransactionInterceptor interceptor) {
//		return new ParamEventAMQPListener(messageTemplate, interceptor);
//	}
	
	@Bean
	public ParamUpdateEventListener paramEventUpdateListener() {
		return new ParamUpdateEventListener();
	}
	
	@Bean
	public DefaultValidatorProvider defaultValidatorProvider(){
		return new DefaultValidatorProvider();
	}
	
	@Bean
	public EntityConfigBuilder entityConfigBuilder(BeanResolverStrategy beanResolver){
		if(typeClassMappings==null) {
			typeClassMappings = new HashMap<>();
		}
		
		if(!typeClassMappings.containsKey(LocalDate.class.getName())) {
			typeClassMappings.put(LocalDate.class.getName(), "date");
		}
		
		return new EntityConfigBuilder(beanResolver, typeClassMappings);
	}
	
	@Bean
	public EntityStateBuilder entityStateBuilder(BeanResolverStrategy beanResolver){
		return new EntityStateBuilder(beanResolver);
	}
	
	@Bean
	public PageNavigationInitializer pageNavigationInitializer(){
		return new PageNavigationInitializer();
	}
	
	@Bean
	public QuadModelBuilder quadModelBuilder(BeanResolverStrategy beanResolver) {
		return new DefaultQuadModelBuilder(beanResolver);
	} 
	
	@Bean
	public SecurityUtils securityUtils() {
		return new SecurityUtils(secureRegex);
	}
	
	
//	@Bean
//	public DefaultQuadModelBuilder quadModelBuilder(DomainConfigBuilder domainConfigApi, EntityStateBuilder stateAndConfigBuilder,
//			ApplicationContext appCtx, PageNavigationInitializer navigationStateHelper,
//			ValidatorProvider validatorProvider, @Qualifier("default.param.state.repository") ParamStateGateway paramStateGateway){
//		return new DefaultQuadModelBuilder(domainConfigApi,stateAndConfigBuilder,appCtx,navigationStateHelper,validatorProvider,paramStateGateway);
//	}
}
