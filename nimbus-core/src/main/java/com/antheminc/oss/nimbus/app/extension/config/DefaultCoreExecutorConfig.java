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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertyResolver;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessageConverter;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutor;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandTransactionInterceptor;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContextLoader;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContextPathVariableResolver;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultActionExecutorConfig;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultActionExecutorDelete;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.FunctionExecutor;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultActionExecutorGet;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultActionExecutorNav;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultActionExecutorNew;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultActionExecutorProcess;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultActionExecutorReplace;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultActionExecutorSearch;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultActionExecutorUpdate;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultBehaviorExecutorState;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultCommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultCommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultExecutionContextLoader;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultExecutionContextPathVariableResolver;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.HierarchyMatchBasedBeanFinder;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.DBSearch;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.MongoSearchByExample;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.MongoSearchByQuery;
import com.antheminc.oss.nimbus.support.expr.ExpressionEvaluator;
import com.antheminc.oss.nimbus.support.expr.SpelExpressionEvaluator;

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
	public CommandTransactionInterceptor commandTransactionInterceptor(BeanResolverStrategy beanResolver){
		return new CommandTransactionInterceptor(beanResolver);
	}
	
	@Bean
	public CommandPathVariableResolver defaultCommandPathVariableResolver(BeanResolverStrategy beanResolver, PropertyResolver propertyResolver) {
		return new DefaultCommandPathVariableResolver(beanResolver, propertyResolver);
	}
	
	@Bean
	public ExecutionContextPathVariableResolver defaultExecutionContextPathVariableResolver(BeanResolverStrategy beanResolver) {
		return new DefaultExecutionContextPathVariableResolver(beanResolver);
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
	
	@Bean(name="default.$state")
	public CommandExecutor<?> defaultActionBehaviorExecutorGetState(BeanResolverStrategy beanResolver){
		return new DefaultBehaviorExecutorState(beanResolver);
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

	@Bean(name="default._search$execute")
	public CommandExecutor<?> defaultActionExecutorSearch(BeanResolverStrategy beanResolver){
		return new DefaultActionExecutorSearch<>(beanResolver);
	}	
	
}
