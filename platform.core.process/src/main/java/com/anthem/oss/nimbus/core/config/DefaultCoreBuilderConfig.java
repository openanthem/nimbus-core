package com.anthem.oss.nimbus.core.config;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.anthem.oss.nimbus.core.domain.command.execution.CommandTransactionInterceptor;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.config.ValidatorProvider;
import com.anthem.oss.nimbus.core.domain.model.config.builder.DefaultValidatorProvider;
import com.anthem.oss.nimbus.core.domain.model.config.builder.EntityConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.builder.DefaultQuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.builder.EntityStateBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.builder.PageNavigationInitializer;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ParamStateGateway;
import com.anthem.oss.nimbus.core.integration.websocket.ParamEventAMQPListener;

/**
 * @author Sandeep Mantha
 * @author Soham Chakravarti
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="domain.model")
public class DefaultCoreBuilderConfig {
	
	private Map<String, String> typeClassMappings;
	
	private List<String> basePackages;
	
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
	public EntityStateBuilder entityStateBuilder(){
		return new EntityStateBuilder();
	}
	
	@Bean
	public PageNavigationInitializer pageNavigationInitializer(){
		return new PageNavigationInitializer();
	}
	
	@Bean(name="default.quadModelBuilder")
	public DefaultQuadModelBuilder quadModelBuilder(DomainConfigBuilder domainConfigApi, EntityStateBuilder stateAndConfigBuilder,
			ApplicationContext appCtx, PageNavigationInitializer navigationStateHelper,
			ValidatorProvider validatorProvider, @Qualifier("default.param.state.repository") ParamStateGateway paramStateGateway){
		return new DefaultQuadModelBuilder(domainConfigApi,stateAndConfigBuilder,appCtx,navigationStateHelper,validatorProvider,paramStateGateway);
	}
}
