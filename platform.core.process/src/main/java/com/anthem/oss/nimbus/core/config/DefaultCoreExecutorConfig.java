package com.antheminc.oss.nimbus.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecutor;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandTransactionInterceptor;
import com.antheminc.oss.nimbus.core.domain.command.execution.DefaultActionBehaviorExecutorGetState;
import com.antheminc.oss.nimbus.core.domain.command.execution.DefaultActionExecutorConfig;
import com.antheminc.oss.nimbus.core.domain.command.execution.DefaultActionExecutorDelete;
import com.antheminc.oss.nimbus.core.domain.command.execution.DefaultActionExecutorGet;
import com.antheminc.oss.nimbus.core.domain.command.execution.DefaultActionExecutorNav;
import com.antheminc.oss.nimbus.core.domain.command.execution.DefaultActionExecutorNew;
import com.antheminc.oss.nimbus.core.domain.command.execution.DefaultActionExecutorProcess;
import com.antheminc.oss.nimbus.core.domain.command.execution.DefaultActionExecutorReplace;
import com.antheminc.oss.nimbus.core.domain.command.execution.DefaultActionExecutorSearch;
import com.antheminc.oss.nimbus.core.domain.command.execution.DefaultActionExecutorUpdate;
import com.antheminc.oss.nimbus.core.domain.command.execution.DefaultCommandExecutorGateway;
import com.antheminc.oss.nimbus.core.domain.command.execution.DefaultCommandPathVariableResolver;
import com.antheminc.oss.nimbus.core.domain.command.execution.DefaultExecutionContextLoader;
import com.antheminc.oss.nimbus.core.domain.command.execution.ExecutionContextLoader;
import com.antheminc.oss.nimbus.core.domain.command.execution.HierarchyMatchBasedBeanFinder;
import com.antheminc.oss.nimbus.core.domain.expr.ExpressionEvaluator;
import com.antheminc.oss.nimbus.core.domain.expr.SpelExpressionEvaluator;
import com.antheminc.oss.nimbus.core.domain.model.state.repo.db.DBSearch;
import com.antheminc.oss.nimbus.core.domain.model.state.repo.db.MongoSearchByExample;
import com.antheminc.oss.nimbus.core.domain.model.state.repo.db.MongoSearchByQuery;
import com.antheminc.oss.nimbus.core.session.impl.HttpSessionCache;
import com.antheminc.oss.nimbus.core.session.impl.SessionCache;

/**
 * @author Sandeep Mantha
 *
 */
@Configuration
public class DefaultCoreExecutorConfig {
	
	@Bean
	public ExpressionEvaluator expressionEvaluator() {
		return new SpelExpressionEvaluator();
	}
	
	@Bean
	public CommandMessageConverter commandMessageConverter(BeanResolverStrategy beanResolver) {
		return new CommandMessageConverter(beanResolver);
	}
	
	@Bean
	public CommandTransactionInterceptor commandTransactionInterceptor(){
		return new CommandTransactionInterceptor();
	}
	
	@Bean
	public CommandPathVariableResolver defaultCommandPathVariableResolver(BeanResolverStrategy beanResolver) {
		return new DefaultCommandPathVariableResolver(beanResolver);
	}
	
	@Bean
	public SessionCache sessionCache() {
		return new HttpSessionCache();
	}
	
	@Bean(name="default.ExecutionContextLoader", destroyMethod="clear") 
	//@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS, scopeName="session")
	public ExecutionContextLoader defaultExecutionContextLoader(BeanResolverStrategy beanResolver) {
		return new DefaultExecutionContextLoader(beanResolver);
	}
	
	@Bean(name="default._new$execute")
	public CommandExecutor<?> defaultActionExecutorNew(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorNew(beanResolver);
	}
	
	@Bean(name="default._get$execute")
	public CommandExecutor<?> defaultActionExecutorGet(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorGet(beanResolver);
	}
	
	@Bean(name="default._nav$execute")
	public CommandExecutor<?> defaultActionExecutorNav(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorNav<>(beanResolver);
	}
	
	@Bean(name="default._process$execute")
	public CommandExecutor<?> defaultActionExecutorProcess(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorProcess<>(beanResolver);
	}
	
	@Bean(name="default._search$execute")
	public CommandExecutor<?> defaultActionExecutorSearch(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorSearch<>(beanResolver);
	}
	
	@Bean(name="default._update$execute")
	public CommandExecutor<?> defaultActionExecutorUpdate(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorUpdate(beanResolver);
	}
	
	@Bean(name="default._delete$execute")
	public CommandExecutor<?> defaultActionProcessExecutorDelete(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorDelete(beanResolver);
	}
	
	@Bean(name="default._replace$execute")
	public CommandExecutor<?> defaultActionProcessExecutorReplace(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorReplace(beanResolver);
	}
	
	@Bean(name="default._config$execute")
	public CommandExecutor<?> defaultBehaviorExecutorConfig(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorConfig(beanResolver);
	}
	
	@Bean(name="default._get$state")
	public CommandExecutor<?> defaultActionBehaviorExecutorGetState(BeanResolverStrategy beanResolver){
		return new DefaultActionBehaviorExecutorGetState(beanResolver);
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
