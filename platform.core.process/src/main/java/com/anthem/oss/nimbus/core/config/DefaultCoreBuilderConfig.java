package com.anthem.oss.nimbus.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.anthem.oss.nimbus.core.domain.command.execution.CommandTransactionInterceptor;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.config.builder.ExecutionInputConfigHandler;
import com.anthem.oss.nimbus.core.domain.config.builder.ExecutionOutputConfigHandler;
import com.anthem.oss.nimbus.core.domain.model.config.ValidatorProvider;
import com.anthem.oss.nimbus.core.domain.model.config.builder.DefaultValidatorProvider;
import com.anthem.oss.nimbus.core.domain.model.config.builder.EntityConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.builder.EntityStateBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.builder.PageNavigationInitializer;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ParamStateGateway;
import com.anthem.oss.nimbus.core.integration.websocket.ParamEventAMQPListener;

/**
 * @author Sandeep Mantha
 *
 */

@Configuration 
public class DefaultCoreBuilderConfig {
	
	@Bean
	public DomainConfigBuilder domainConfigBuilder(EntityConfigBuilder modelConfigBuilder){
		return new DomainConfigBuilder(modelConfigBuilder);
	}
	
	@Bean
	public ExecutionInputConfigHandler executionInputConfigHandler(){
		return new ExecutionInputConfigHandler();
	}
	
	@Bean
	public ExecutionOutputConfigHandler executionOutputConfigHandler(){
		return new ExecutionOutputConfigHandler();
	}
	
	@Bean
	public ParamEventAMQPListener paramEventAMQPListener(SimpMessageSendingOperations messageTemplate,CommandTransactionInterceptor interceptor) {
		return new ParamEventAMQPListener(messageTemplate, interceptor);
	}
	
	//model config builder
	@Bean
	public DefaultValidatorProvider defaultValidatorProvider(){
		return new DefaultValidatorProvider();
	}
	
	@Bean
	public EntityConfigBuilder entityConfigBuilder(ExecutionInputConfigHandler execInputHandler,
			ExecutionOutputConfigHandler execOutputHandler){
		return new EntityConfigBuilder(execInputHandler,execOutputHandler);
	}
	
//	// state builder
	@Bean
	public EntityStateBuilder entityStateBuilder(){
		return new EntityStateBuilder();
	}
	
	@Bean
	public PageNavigationInitializer pageNavigationInitializer(){
		return new PageNavigationInitializer();
	}
	
	@Bean(name="default.quadModelBuilder")
	public QuadModelBuilder quadModelBuilder(DomainConfigBuilder domainConfigApi, EntityStateBuilder stateAndConfigBuilder,
			ApplicationContext appCtx, PageNavigationInitializer navigationStateHelper,
			ValidatorProvider validatorProvider, @Qualifier("default.param.state.repository") ParamStateGateway paramStateGateway){
		return new QuadModelBuilder(domainConfigApi,stateAndConfigBuilder,appCtx,navigationStateHelper,validatorProvider,paramStateGateway);
	}
}
