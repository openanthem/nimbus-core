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
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultBehaviorExecutorConfig;
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultCommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultExecutionContextLoader;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContextLoader;
import com.anthem.oss.nimbus.core.domain.command.execution.HierarchyMatchBasedBeanFinder;
import com.anthem.oss.nimbus.core.domain.command.execution.ParamCodeValueProvider;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;

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
	public DefaultActionExecutorProcess defaultActionExecutorNav(){
		return new DefaultActionExecutorProcess();
	}
	
	
	@Bean(name="default._process$execute")
	public DefaultActionExecutorProcess defaultActionExecutorProcess(){
		return new DefaultActionExecutorProcess();
	}
	
	@Bean(name="default._search$execute")
	public DefaultActionExecutorSearch defaultActionExecutorSearch(ModelRepositoryFactory repFactory, DomainConfigBuilder domainConfigApi,
			CommandMessageConverter converter){
		return new DefaultActionExecutorSearch();
	}
	
	@Bean(name="default._update$execute")
	public DefaultActionExecutorUpdate defaultActionExecutorUpdate(){
		return new DefaultActionExecutorUpdate();
	}
	
	@Bean(name="default._delete$execute")
	public DefaultActionProcessExecutorDelete defaultActionProcessExecutorDelete(ModelRepositoryFactory repoFactory, DomainConfigBuilder domainConfigApi){
		return new DefaultActionProcessExecutorDelete();
	}
	
	@Bean(name="default._replace$execute")
	public DefaultActionProcessExecutorReplace defaultActionProcessExecutorReplace(){
		return new DefaultActionProcessExecutorReplace();
	}
	
	@Bean(name="default.$config")
	public DefaultBehaviorExecutorConfig defaultBehaviorExecutorConfig(){
		return new DefaultBehaviorExecutorConfig();
	}
	
	@Bean(name="default.$nav")
	public DefaultActionExecutorNav defaultBehaviorExecutorNav() {
		return new DefaultActionExecutorNav();
	}

	@Bean
	public HierarchyMatchBasedBeanFinder hierarchyMatchBasedBeanFinder(){
		return new HierarchyMatchBasedBeanFinder();
	}
	
	@Bean(name="default.processGateway")
	public DefaultCommandExecutorGateway defaultProcessGateway(BeanResolverStrategy beanResolver){
		return new DefaultCommandExecutorGateway(beanResolver);
	}
	
	@Bean
	public ParamCodeValueProvider paramCodeValueProvider(DefaultActionExecutorSearch searchExecutor){
		return new ParamCodeValueProvider(searchExecutor);
	}
}
