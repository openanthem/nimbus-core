package com.anthem.oss.nimbus.core.config;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.DefaultBeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandTransactionInterceptor;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.config.builder.DefaultValidatorProvider;
import com.anthem.oss.nimbus.core.domain.model.config.builder.EntityConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.builder.DefaultQuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.builder.EntityStateBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.builder.PageNavigationInitializer;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.integration.websocket.ParamEventAMQPListener;

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
	
	@Bean
	public BeanResolverStrategy defaultBeanResolver(ApplicationContext appCtx) {
		return new DefaultBeanResolverStrategy(appCtx);
	}
	
	@Bean
	public DomainConfigBuilder domainConfigBuilder(EntityConfigBuilder configBuilder){
		return new DomainConfigBuilder(configBuilder, basePackages);
	}
	
	@Bean
	public ParamEventAMQPListener paramEventAMQPListener(SimpMessageSendingOperations messageTemplate,CommandTransactionInterceptor interceptor) {
		return new ParamEventAMQPListener(messageTemplate, interceptor);
	}
	
	@Bean
	public DefaultValidatorProvider defaultValidatorProvider(){
		return new DefaultValidatorProvider();
	}
	
	@Bean
	public EntityConfigBuilder entityConfigBuilder(){
		if(typeClassMappings==null) {
			typeClassMappings = new HashMap<>();
		}
		
		if(!typeClassMappings.containsKey(LocalDate.class.getName())) {
			typeClassMappings.put(LocalDate.class.getName(), "date");
		}
		
		return new EntityConfigBuilder(typeClassMappings);
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
	
//	@Bean
//	public DefaultQuadModelBuilder quadModelBuilder(DomainConfigBuilder domainConfigApi, EntityStateBuilder stateAndConfigBuilder,
//			ApplicationContext appCtx, PageNavigationInitializer navigationStateHelper,
//			ValidatorProvider validatorProvider, @Qualifier("default.param.state.repository") ParamStateGateway paramStateGateway){
//		return new DefaultQuadModelBuilder(domainConfigApi,stateAndConfigBuilder,appCtx,navigationStateHelper,validatorProvider,paramStateGateway);
//	}
}
