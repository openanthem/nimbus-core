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
package com.antheminc.oss.nimbus.app.extension.config;


import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.client.RestTemplate;

import com.antheminc.oss.nimbus.channel.messagequeue.MessageQueueCommandDispatcher;
import com.antheminc.oss.nimbus.channel.web.RemoteModelClientHttpRequestInterceptor;
import com.antheminc.oss.nimbus.channel.web.WebActionController;
import com.antheminc.oss.nimbus.channel.web.WebCommandBuilder;
import com.antheminc.oss.nimbus.channel.web.WebCommandDispatcher;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.ClassPropertyConverter;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.state.repo.DefaultModelRepositoryFactory;
import com.antheminc.oss.nimbus.domain.model.state.repo.DefaultParamStateRepositoryDetached;
import com.antheminc.oss.nimbus.domain.model.state.repo.DefaultParamStateRepositoryLocal;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;
import com.antheminc.oss.nimbus.domain.model.state.repo.ParamStateRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ParamStateRepositoryGateway;
import com.antheminc.oss.nimbus.domain.model.state.repo.SpringSecurityAuditorAware;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.ParamStateAtomicPersistenceEventListener;
import com.antheminc.oss.nimbus.domain.model.state.repo.ws.DefaultWSModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ws.ParamStateAtomicRemotePersistenceEventListener;
import com.antheminc.oss.nimbus.domain.model.state.repo.ws.RemoteWSModelRepository;
import com.antheminc.oss.nimbus.domain.rules.DefaultRulesEngineFactoryProducer;
import com.antheminc.oss.nimbus.domain.rules.drools.DecisionTableConfigBuilder;
import com.antheminc.oss.nimbus.domain.rules.drools.DrlConfigBuilder;
import com.antheminc.oss.nimbus.domain.rules.drools.DroolsRulesEngineFactory;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandler;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandlerReflection;

/**
 * @author Sandeep Mantha
 *
 */
@Configuration
@ComponentScan(basePackageClasses = WebActionController.class)
@EnableCaching
public class DefaultCoreConfiguration {
	
	@Bean(name="default.repositoryFactory")
	public DefaultModelRepositoryFactory defaultModelRepositoryFactory(BeanResolverStrategy beanResolver){
		/*Add ModelRepository implementation beans to a lookup map*/
		Map<String, ModelRepository> repoBeanLookup = new HashMap<>();

		// MongoDB
		Optional.ofNullable(beanResolver.find(ModelRepository.class, Repo.Database.rep_mongodb.name()))
			.ifPresent(repo->repoBeanLookup.put(Repo.Database.rep_mongodb.name(), repo));
		
		// RDBMS
		Optional.ofNullable(beanResolver.find(ModelRepository.class, Repo.Database.rep_rdbms.name()))
			.ifPresent(repo->repoBeanLookup.put(Repo.Database.rep_rdbms.name(), repo));
		
		
		repoBeanLookup.put(Repo.Remote.rep_remote_ws.name(), beanResolver.get(ModelRepository.class, Repo.Remote.rep_remote_ws.name()));
		repoBeanLookup.put(Repo.Database.rep_ws.name(), beanResolver.get(ModelRepository.class, Repo.Database.rep_ws.name()));

		return new DefaultModelRepositoryFactory(beanResolver, repoBeanLookup);
	}

	@Bean(name="default.rep_ws")
	public DefaultWSModelRepository defaultWSModelRepository(BeanResolverStrategy beanResolver, RestTemplateBuilder builder){
		RestTemplate restTemplate = beanResolver.get(RestTemplate.class, "rep_ws.restTemplate");
		return new DefaultWSModelRepository(beanResolver, restTemplate);
	}
	
	@Bean(name="default.rep_remote_ws")
	public RemoteWSModelRepository remoteWSModelRepository(BeanResolverStrategy beanResolver){
		RestTemplate restTemplate = beanResolver.get(RestTemplate.class, "rep_remote_ws.restTemplate");
		return new RemoteWSModelRepository(beanResolver, restTemplate);
	}
	
	@Bean(name="default.rep_ws.restTemplate")
	public RestTemplate wsRestTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.build();
		return restTemplate;
	}
	
	@Bean(name="default.rep_remote_ws.restTemplate")
	public RestTemplate remoteWSRestTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.additionalInterceptors(new RemoteModelClientHttpRequestInterceptor()).build();
		return restTemplate;
	}
	
	@Bean(name="default.paramStateAtomicPersistenceEventListener")
	public ParamStateAtomicPersistenceEventListener paramStateAtomicPersistenceEventListener(ModelRepositoryFactory repoFactory){
		return new ParamStateAtomicPersistenceEventListener(repoFactory);
	}
	
	@Bean(name="default.paramStateAtomicRemotePersistenceEventListener")
	public ParamStateAtomicRemotePersistenceEventListener paramStateAtomicRemotePersistenceEventListener(ModelRepositoryFactory repoFactory){
		return new ParamStateAtomicRemotePersistenceEventListener(repoFactory);
	}
	
	
	@Bean(name="default.param.state.rep_local")
	public DefaultParamStateRepositoryLocal defaultParamStateRepositoryLocal(JavaBeanHandler javaBeanHandler){
		return new DefaultParamStateRepositoryLocal(javaBeanHandler);
	}
	
	@Bean(name="default.param.state.rep_detached")
	public DefaultParamStateRepositoryDetached defaultParamStateRepositoryDetached(BeanResolverStrategy beanResolver){
		return new DefaultParamStateRepositoryDetached(beanResolver);
	}
	
	@Bean(name="default.param.state.repository")
	public ParamStateRepositoryGateway paramStateRepositoryGateway(JavaBeanHandler javaBeanHandler, @Qualifier("default.param.state.rep_local") ParamStateRepository local, BeanResolverStrategy beanResolver){
		return new ParamStateRepositoryGateway(javaBeanHandler,local, beanResolver);
	}
	
	//web socket
	
	//rules drools
	@Bean(name="rules.factory.drools")
	public DroolsRulesEngineFactory droolsRulesEngineFactory(BeanResolverStrategy beanResolver){
		return new DroolsRulesEngineFactory(beanResolver);
	}
	
	@Bean(name="rules.factory.drools.drl")
	public DrlConfigBuilder drlConfigBuilder(){
		return new DrlConfigBuilder();
	}
	
	@Bean(name="rules.factory.drools.dtable")
	public DecisionTableConfigBuilder dtableConfigBuilder(){
		return new DecisionTableConfigBuilder();
	}
	
	@Bean(name="default.rules.factory.producer")
	public DefaultRulesEngineFactoryProducer defaultRulesEngineFactoryProducer(){
		return new DefaultRulesEngineFactoryProducer();
	}
	
	@Bean(name="default.java.bean.handler")
	public JavaBeanHandler javaBeanHandler(){
		return new JavaBeanHandlerReflection();
	}
	
	//web
	@Bean
	public WebCommandBuilder webCommandBuilder(){
		return new WebCommandBuilder();
	}
	
	@Bean
	public WebCommandDispatcher webCommandDispatcher(BeanResolverStrategy beanResolver){
		return new WebCommandDispatcher(beanResolver);
	}
	
	@Bean
	public MessageQueueCommandDispatcher messageQueueCommandDispatcher(BeanResolverStrategy beanResolver){
		return new MessageQueueCommandDispatcher(beanResolver);
	}

	@Bean
	public AuditorAware<String> auditorProvider(BeanResolverStrategy beanResolver) {
		return new SpringSecurityAuditorAware(beanResolver);
	}
	
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Bean
	public ClassPropertyConverter classPropertyConverter() {
		return new ClassPropertyConverter();
	}
	
	@Bean("default.zdt.provider")
	public DateTimeProvider defaultZDTProvider() {
		return new DateTimeProvider() {
			
			@Override
			public Optional<TemporalAccessor> getNow() {
				return Optional.of(ZonedDateTime.now());
			}
		};
	}
	
}
