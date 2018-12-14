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

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Constraint;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.PropertyResolver;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.context.DefaultBeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.FunctionExecutor;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.process.ParamUpdateEventListener;
import com.antheminc.oss.nimbus.domain.config.builder.AnnotationAttributeHandler;
import com.antheminc.oss.nimbus.domain.config.builder.AnnotationConfigHandler;
import com.antheminc.oss.nimbus.domain.config.builder.DefaultAnnotationConfigHandler;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.config.builder.EventAnnotationConfigHandler;
import com.antheminc.oss.nimbus.domain.config.builder.attributes.ConstraintAnnotationAttributeHandler;
import com.antheminc.oss.nimbus.domain.config.builder.attributes.DefaultAnnotationAttributeHandler;
import com.antheminc.oss.nimbus.domain.model.config.builder.EntityConfigBuilder;
import com.antheminc.oss.nimbus.domain.model.config.builder.internal.DefaultEntityConfigBuilder;
import com.antheminc.oss.nimbus.domain.model.config.builder.internal.DefaultExecutionConfigProvider;
import com.antheminc.oss.nimbus.domain.model.config.builder.internal.DetourExecutionConfigProvider;
import com.antheminc.oss.nimbus.domain.model.config.builder.internal.EventHandlerConfigFactory;
import com.antheminc.oss.nimbus.domain.model.config.builder.internal.ExecutionConfigFactory;
import com.antheminc.oss.nimbus.domain.model.config.internal.DefaultValidatorProvider;
import com.antheminc.oss.nimbus.domain.model.state.builder.EntityStateBuilder;
import com.antheminc.oss.nimbus.domain.model.state.builder.QuadModelBuilder;
import com.antheminc.oss.nimbus.domain.model.state.builder.internal.DefaultEntityStateBuilder;
import com.antheminc.oss.nimbus.domain.model.state.builder.internal.DefaultQuadModelBuilder;
import com.antheminc.oss.nimbus.domain.model.state.extension.ChangeLogCommandEventHandler;
import com.antheminc.oss.nimbus.support.DefaultLoggingInterceptor;
import com.antheminc.oss.nimbus.support.SecurityUtils;

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
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class DefaultCoreBuilderConfig {
	
	private Map<String, String> typeClassMappings;
	
	private List<String> basePackages;
	
	private List<String> basePackagesToExclude;
	
	@Value("${platform.config.secure.regex}")
	private String secureRegex;
	
	
	@Bean	
	public BeanResolverStrategy defaultBeanResolver(ApplicationContext appCtx) {
		return new DefaultBeanResolverStrategy(appCtx);
	}
	
	@Bean
	public ChangeLogCommandEventHandler changeLogCommandEventHandler(BeanResolverStrategy beanResolver) {
		return new ChangeLogCommandEventHandler(beanResolver);
	}
	
	@Bean
	public DomainConfigBuilder domainConfigBuilder(EntityConfigBuilder configBuilder){
		return new DomainConfigBuilder(configBuilder, basePackages, basePackagesToExclude);
	}
	
	@Bean
	public FunctionExecutor<?,?> functionExecutor(BeanResolverStrategy beanResolver){
		return new FunctionExecutor<>(beanResolver);
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
	public EventHandlerConfigFactory eventHandlerConfigFactory(BeanResolverStrategy beanResolver) {
		return new EventHandlerConfigFactory(beanResolver);
	}
	
	@Bean
	public ExecutionConfigFactory executionConfigFactory(BeanResolverStrategy beanResolver) {
		return new ExecutionConfigFactory();
	}
	
	@Bean 
	public DefaultExecutionConfigProvider defaultExecutionConfigProvider(BeanResolverStrategy beanResolver) {
		return new DefaultExecutionConfigProvider();
	}
	
	@Bean 
	public DetourExecutionConfigProvider detourExecutionConfigProvider(BeanResolverStrategy beanResolver) {
		return new DetourExecutionConfigProvider();
	}
	
	@Bean(name="default.annotationConfigBuilder")
	public AnnotationConfigHandler annotationConfigHandler(BeanResolverStrategy beanResolver, PropertyResolver propertyResolver) {
		Map<Class<? extends Annotation>, AnnotationAttributeHandler> attributeHandlers = new HashMap<>();
		attributeHandlers.put(Constraint.class, new ConstraintAnnotationAttributeHandler(beanResolver));
		
		return new DefaultAnnotationConfigHandler(new DefaultAnnotationAttributeHandler(), attributeHandlers, propertyResolver);
	}
	
	@Bean(name="default.eventAnnotationConfigBuilder")
	public AnnotationConfigHandler eventAnnotationConfigHandler() {
		return new EventAnnotationConfigHandler();
	}
	
	@Bean
	public EntityConfigBuilder entityConfigBuilder(BeanResolverStrategy beanResolver){
		if(typeClassMappings==null) {
			typeClassMappings = new HashMap<>();
		}
		
		return new DefaultEntityConfigBuilder(beanResolver, typeClassMappings);
	}
	
	@Bean
	public EntityStateBuilder entityStateBuilder(BeanResolverStrategy beanResolver){
		return new DefaultEntityStateBuilder(beanResolver);
	}
	
	@Bean
	public QuadModelBuilder quadModelBuilder(BeanResolverStrategy beanResolver) {
		return new DefaultQuadModelBuilder(beanResolver);
	} 
	
	@Bean
	public SecurityUtils securityUtils() {
		return new SecurityUtils(secureRegex);
	}
	
	@Bean
	public DefaultLoggingInterceptor defaultLoggingHandler() {
		return new DefaultLoggingInterceptor();
	}

}
