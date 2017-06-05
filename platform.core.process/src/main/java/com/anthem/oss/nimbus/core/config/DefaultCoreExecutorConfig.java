package com.anthem.oss.nimbus.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandTransactionInterceptor;
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultActionExecutorGet;
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultActionExecutorNav;
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultActionExecutorNew;
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultActionExecutorProcess;
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultActionExecutorSearch;
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultActionExecutorUpdate;
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultActionProcessExecutorDelete;
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultActionProcessExecutorReplace;
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultCommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultExecutionContextLoader;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContextLoader;
import com.anthem.oss.nimbus.core.domain.command.execution.HierarchyMatchBasedBeanFinder;
import com.anthem.oss.nimbus.core.domain.command.execution.ParamCodeValueProvider;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.DBSearch;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.MongoSearchByExample;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.MongoSearchByQuery;

/**
 * @author Sandeep Mantha
 *
 */
@Configuration
public class DefaultCoreExecutorConfig {
	
	@Bean
	public CommandMessageConverter commandMessageConverter(){
		return new CommandMessageConverter();
	}
	
	@Bean
	public CommandTransactionInterceptor commandTransactionInterceptor(){
		return new CommandTransactionInterceptor();
	}
	
	@Bean
	public ExecutionContextLoader defaultExecutionContextLoader(BeanResolverStrategy beanResolver) {
		return new DefaultExecutionContextLoader(beanResolver);
	}
	
	@Bean(name="default._new$execute")
	public DefaultActionExecutorNew defaultActionExecutorNew(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorNew(beanResolver);
	}
	
	@Bean(name="default._get$execute")
	public DefaultActionExecutorGet defaultActionExecutorGet(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorGet(beanResolver);
	}
	

	@Bean(name="default._nav$execute")
	public DefaultActionExecutorNav defaultActionExecutorNav(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorNav(beanResolver);
	}
	
	@Bean(name="default._process$execute")
	public DefaultActionExecutorProcess defaultActionExecutorProcess(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorProcess(beanResolver);
	}
	
	@Bean(name="default._search$execute")
	public DefaultActionExecutorSearch defaultActionExecutorSearch(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorSearch(beanResolver);
	}
	
	@Bean(name="default._update$execute")
	public DefaultActionExecutorUpdate defaultActionExecutorUpdate(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorUpdate(beanResolver);
	}
	
	@Bean(name="default._delete$execute")
	public DefaultActionProcessExecutorDelete defaultActionProcessExecutorDelete(ModelRepositoryFactory repoFactory, DomainConfigBuilder domainConfigApi){
		return new DefaultActionProcessExecutorDelete();
	}
	
	@Bean(name="default._replace$execute")
	public DefaultActionProcessExecutorReplace defaultActionProcessExecutorReplace(){
		return new DefaultActionProcessExecutorReplace();
	}
	
	@Bean
	public HierarchyMatchBasedBeanFinder hierarchyMatchBasedBeanFinder(){
		return new HierarchyMatchBasedBeanFinder();
	}
	
	
	@Bean(name="default.processGateway")
	public DefaultCommandExecutorGateway defaultProcessGateway(BeanResolverStrategy beanResolver){
		return new DefaultCommandExecutorGateway(beanResolver);
	}
	
	@Bean(name="searchByExample")
	public DBSearch searchByExample(BeanResolverStrategy beanResolver) {
		return new MongoSearchByExample(beanResolver);
	}
	
	@Bean(name="searchByQuery")
	public DBSearch searchByQuery(BeanResolverStrategy beanResolver) {
		return new MongoSearchByQuery(beanResolver);
	}
	
}
