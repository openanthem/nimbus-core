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


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.client.RestTemplate;

import com.anthem.nimbus.platform.core.process.api.repository.SessionCacheRepository;
import com.anthem.nimbus.platform.core.process.api.support.ProcessBeanHelper;
import com.anthem.nimbus.platform.core.process.mq.MessageReceiver;
import com.anthem.oss.nimbus.core.domain.model.state.builder.ValidationConfigHandler;
import com.anthem.oss.nimbus.core.domain.model.state.repo.DefaultModelRepositoryFactory;
import com.anthem.oss.nimbus.core.domain.model.state.repo.DefaultParamStateRepositoryDetached;
import com.anthem.oss.nimbus.core.domain.model.state.repo.DefaultParamStateRepositoryLocal;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ModelPersistenceHandler;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ModelRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ModelRepositoryFactory;
import com.anthem.oss.nimbus.core.domain.model.state.repo.MongoIdSequenceRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ParamStateRepositoryGateway;
import com.anthem.oss.nimbus.core.domain.model.state.repo.SpringSecurityAuditorAware;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ClientUserGrooupSearchResponseConverter;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ParamStateAtomicPersistenceEventListener;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.mongo.DefaultMongoModelPersistenceHandler;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.mongo.DefaultMongoModelRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ws.DefaultWSModelRepository;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.core.utils.ProcessBeanResolver;
import com.antheminc.oss.nimbus.channel.web.WebActionController;
import com.antheminc.oss.nimbus.channel.web.WebCommandBuilder;
import com.antheminc.oss.nimbus.channel.web.WebCommandDispatcher;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.state.repo.IdSequenceRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ParamStateRepository;
import com.antheminc.oss.nimbus.domain.rules.DefaultRulesEngineFactoryProducer;
import com.antheminc.oss.nimbus.domain.rules.drools.DroolsRulesEngineFactory;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandler;
import com.antheminc.oss.nimbus.support.pojo.reflection.JavaBeanHandlerReflection;

/**
 * @author Sandeep Mantha
 *
 */
@Configuration
@EnableMongoAuditing
@ComponentScan(basePackageClasses = WebActionController.class)
public class DefaultCoreConfiguration {
	
	//TODO - the below 2 beans are blank. should they be deleted ?
	@Bean(name="default.param.state.rep_session")
	public SessionCacheRepository customessionCacheRepository(){
		return new SessionCacheRepository();
	}
	
	
	@Bean(name="default.param.state.rep_session")
	public SessionCacheRepository sessionCacheRepository(){
		return new SessionCacheRepository();
	}
	
	@Bean(name="default.processBeanHelper")
	public ProcessBeanHelper processBeanHelper(){
		return new ProcessBeanHelper();
	}

	@Bean(name="default.messageReciever")
	public MessageReceiver messageReciever(){
		return new MessageReceiver();
	}
	
//	//TODO - a blank class - probably have to delete
	@Bean(name="default.validationConfigHandler")
	public ValidationConfigHandler validationConfigHandler(){
		return new ValidationConfigHandler();
	}
	
	@Bean
	public DefaultModelRepositoryFactory defaultModelRepositoryFactory(BeanResolverStrategy beanResolver){
		return new DefaultModelRepositoryFactory(beanResolver);
	}
	
	@Bean(name="default.rep_mongodb_handler")
	public DefaultMongoModelPersistenceHandler defaultMongoModelPersistenceHandler(@Qualifier("default.rep_mongodb") ModelRepository rep){
		return new DefaultMongoModelPersistenceHandler(rep);
	}
	
	@Bean(name="default.rep_mongodb")
	public DefaultMongoModelRepository defaultMongoModelRepository(MongoOperations mongoOps, IdSequenceRepository idSequenceRepo, BeanResolverStrategy beanResolver){
		return new DefaultMongoModelRepository(mongoOps, idSequenceRepo, beanResolver);
	}
	
	@Bean(name="default.rep_ws")
	public DefaultWSModelRepository defaultWSModelRepository(BeanResolverStrategy beanResolver){
		return new DefaultWSModelRepository(beanResolver);
	}
	
	@Bean(name="default.paramStateAtomicPersistenceEventListener")
	public ParamStateAtomicPersistenceEventListener paramStateAtomicPersistenceEventListener(ModelRepositoryFactory repoFactory,@Qualifier("default.rep_mongodb_handler") ModelPersistenceHandler handler){
		return new ParamStateAtomicPersistenceEventListener(repoFactory, handler);
	}
	
//	@Bean(name="default.paramStateBatchPersistenceEventListener")
//	public ParamStateBulkPersistenceEventListener paramStateBatchPersistenceEventListener(ModelRepositoryFactory repoFactory){
//		return new ParamStateBulkPersistenceEventListener(repoFactory);
//	}
	
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
	public DroolsRulesEngineFactory droolsRulesEngineFactory(){
		return new DroolsRulesEngineFactory();
	}
	
	@Bean(name="default.rules.factory.producer")
	public DefaultRulesEngineFactoryProducer defaultRulesEngineFactoryProducer(){
		return new DefaultRulesEngineFactoryProducer();
	}
	
//	//utils
	@Bean(name="default.java.bean.handler")
	public JavaBeanHandlerReflection javaBeanHandlerReflection(){
		return new JavaBeanHandlerReflection();
	}
	
	@Bean
	public ProcessBeanResolver processBeanResolver(){
		return new ProcessBeanResolver();
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
	public MongoIdSequenceRepository mongoIdSequenceRepository(MongoOperations mongoOperations){
		return new MongoIdSequenceRepository(mongoOperations);
	}

//	@Bean(name="clientEntityRepo")
//	public ClientEntityRepoService clientEntityRepoService(ClientRepository cRepo, ClientEntityRepository ceRepo, ClientUserRoleRepository crRepo) {
//		return new ClientEntityRepoService(cRepo, ceRepo, crRepo);
//	}
	
//	@Bean(name="clientuserrepo")
//	public ClientUserRepoService clientUserRepoService(ClientUserRepository cuRepo, ClientRepository cRepo, PlatformUserRepository puRepo) {
//		return new ClientUserRepoService(cuRepo, cRepo, puRepo);
//	}
	
//	@Bean(name="clientUserGroupRepo")
//	public ClientUserGroupRepoService clientUserGroupRepoService(ClientEntityRepository ceRepo, ClientUserGroupRepository cugRepo) {
//		return new ClientUserGroupRepoService(ceRepo, cugRepo);
//	}
	
	@Bean
	@Scope(scopeName="session")
	public UserEndpointSession userEndpointSession() {
		return new UserEndpointSession();
	}
	
//	@Bean
//	BeforeSaveListener beforeSaveListener(@Qualifier("default.processGateway") ProcessGateway processGateway) {
//		return new BeforeSaveListener();
//	}
	
	@Bean(name="clientUserGrooupSearchResponseConverter")
	Converter clientUserGroupConverter() {
		return new ClientUserGrooupSearchResponseConverter();
	}
	
	@Bean
	public AuditorAware<String> auditorProvider() {
		return new SpringSecurityAuditorAware();
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
}
