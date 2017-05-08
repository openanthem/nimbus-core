package com.anthem.oss.nimbus.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoOperations;

import com.anthem.nimbus.platform.core.process.api.repository.SessionCacheRepository;
import com.anthem.nimbus.platform.core.process.api.support.ProcessBeanHelper;
import com.anthem.nimbus.platform.core.process.mq.MessageReceiver;
import com.anthem.oss.nimbus.core.bpm.activiti.ModelInstantiationServiceTaskDelegate;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessGateway;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.builder.ValidationConfigHandler;
import com.anthem.oss.nimbus.core.domain.model.state.repo.DefaultParamStateRepositoryLocal;
import com.anthem.oss.nimbus.core.domain.model.state.repo.IdSequenceRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.MongoIdSequenceRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ParamStateRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ParamStateRepositoryGateway;
import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientEntityRepoService;
import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientEntityRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientUserGroupRepoService;
import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientUserGroupRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientUserRepoService;
import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientUserRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientUserRoleRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.PlatformUserRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.DefaultModelRepositoryFactory;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.DefaultMongoModelPersistenceHandler;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.DefaultMongoModelRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelPersistenceHandler;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ParamStateAtomicPersistenceEventListener;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ParamStateBatchPersistenceEventListener;
import com.anthem.oss.nimbus.core.integration.sa.DefaultExecutionHandler;
import com.anthem.oss.nimbus.core.integration.sa.DefaultRuleBasedRequestHandler;
import com.anthem.oss.nimbus.core.integration.sa.DefaultRuleBasedResponseHandler;
import com.anthem.oss.nimbus.core.integration.sa.ProcessExecutionCtxHelper;
import com.anthem.oss.nimbus.core.integration.sa.ServiceExecutionDelegate;
import com.anthem.oss.nimbus.core.integration.sa.ServiceExecutionHelper;
import com.anthem.oss.nimbus.core.rules.DefaultRulesEngineFactoryProducer;
import com.anthem.oss.nimbus.core.rules.drools.DroolsRulesEngineFactory;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.core.utils.JavaBeanHandler;
import com.anthem.oss.nimbus.core.utils.JavaBeanHandlerReflection;
import com.anthem.oss.nimbus.core.utils.ProcessBeanResolver;
import com.anthem.oss.nimbus.core.web.WebActionController;
import com.anthem.oss.nimbus.core.web.WebCommandBuilder;
import com.anthem.oss.nimbus.core.web.WebCommandDispatcher;

/**
 * @author Sandeep Mantha
 *
 */

@Configuration
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
	
//	//repo db
	@Bean(name="default.modelRepositoryFactory")
	public DefaultModelRepositoryFactory defaultModelRepositoryFactory(DomainConfigBuilder domainConfigApi){
		return new DefaultModelRepositoryFactory(domainConfigApi);
	}
	
	@Bean(name="rep_mongodb_handler")
	public DefaultMongoModelPersistenceHandler defaultMongoModelPersistenceHandler(@Qualifier("rep_mongodb") ModelRepository rep){
		return new DefaultMongoModelPersistenceHandler(rep);
	}
	
	@Bean(name="rep_mongodb")
	public DefaultMongoModelRepository defaultMongoModelRepository(MongoOperations mongoOps, IdSequenceRepository idSequenceRepo){
		return new DefaultMongoModelRepository(mongoOps,idSequenceRepo);
	}
	
	@Bean(name="default.paramStateAtomicPersistenceEventListener")
	public ParamStateAtomicPersistenceEventListener paramStateAtomicPersistenceEventListener(ModelRepositoryFactory repoFactory,@Qualifier("rep_mongodb_handler") ModelPersistenceHandler handler){
		return new ParamStateAtomicPersistenceEventListener(repoFactory, handler);
	}
	
	@Bean(name="default.paramStateBatchPersistenceEventListener")
	public ParamStateBatchPersistenceEventListener paramStateBatchPersistenceEventListener(ModelRepositoryFactory repoFactory){
		return new ParamStateBatchPersistenceEventListener(repoFactory);
	}
	
	@Bean(name="default.param.state.rep_local")
	public DefaultParamStateRepositoryLocal defaultParamStateRepositoryLocal(JavaBeanHandler javaBeanHandler){
		return new DefaultParamStateRepositoryLocal(javaBeanHandler);
	}
	
	@Bean(name="default.param.state.repository")
	public ParamStateRepositoryGateway paramStateRepositoryGateway(JavaBeanHandler javaBeanHandler, @Qualifier("default.param.state.rep_local") ParamStateRepository local){
		return new ParamStateRepositoryGateway(javaBeanHandler,local);
	}
	
	//integration sa
	@Bean(name="default.executionhandler")
	public DefaultExecutionHandler defaultExecutionHandler(){
		return new DefaultExecutionHandler();
	}
	
	@Bean(name="default.rulebasedRequesthandler")
	public DefaultRuleBasedRequestHandler defaultRuleBasedRequestHandler(){
		return new DefaultRuleBasedRequestHandler();
	}
	
	@Bean(name="default.rulebasedResponsehandler")
	public DefaultRuleBasedResponseHandler defaultRuleBasedResponseHandler(){
		return new DefaultRuleBasedResponseHandler();
	}
	
	@Bean(name="processExecutionCtx")
	public ProcessExecutionCtxHelper processExecutionCtxHelper(){
		return new ProcessExecutionCtxHelper();
	}
	
	@Bean(name="default.serviceExecutionDelegate")
	@Scope("prototype")
	public ServiceExecutionDelegate serviceExecutionDelegate(ServiceExecutionHelper helper){
		return new ServiceExecutionDelegate();
	}
	
	@Bean
	ModelInstantiationServiceTaskDelegate modelInstantiationServiceTaskDelegate(@Qualifier("default.processGateway") ProcessGateway processGateway){
		return new ModelInstantiationServiceTaskDelegate(processGateway);
	}
	
	@Bean(name="executionHelper")
	public ServiceExecutionHelper serviceExecutionHelper(){
		return new ServiceExecutionHelper();
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
	public WebCommandDispatcher webCommandDispatcher(WebCommandBuilder builder, @Qualifier("default.processGateway") ProcessGateway processGateway){
		return new WebCommandDispatcher(builder,processGateway);
	}
	
	@Bean
	public MongoIdSequenceRepository mongoIdSequenceRepository(MongoOperations mongoOperations){
		return new MongoIdSequenceRepository(mongoOperations);
	}

	@Bean(name="clientEntityRepo")
	public ClientEntityRepoService clientEntityRepoService(ClientRepository cRepo, ClientEntityRepository ceRepo, ClientUserRoleRepository crRepo) {
		return new ClientEntityRepoService(cRepo, ceRepo, crRepo);
	}
	
	@Bean(name="clientuserrepo")
	public ClientUserRepoService clientUserRepoService(ClientUserRepository cuRepo, ClientRepository cRepo, PlatformUserRepository puRepo) {
		return new ClientUserRepoService(cuRepo, cRepo, puRepo);
	}
	
	@Bean(name="clientUserGroupRepo")
	public ClientUserGroupRepoService clientUserGroupRepoService(ClientEntityRepository ceRepo, ClientUserGroupRepository cugRepo) {
		return new ClientUserGroupRepoService(ceRepo, cugRepo);
	}
	
	@Bean
	@Scope(scopeName="session")
	public UserEndpointSession userEndpointSession() {
		return new UserEndpointSession();
	}
	
//	@Bean
//	BeforeSaveListener beforeSaveListener(@Qualifier("default.processGateway") ProcessGateway processGateway) {
//		return new BeforeSaveListener();
//	}
}
