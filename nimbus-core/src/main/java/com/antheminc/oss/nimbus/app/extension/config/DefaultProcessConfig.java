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

import org.activiti.engine.impl.el.ExpressionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.bpm.BPMGateway;
import com.antheminc.oss.nimbus.domain.bpm.activiti.ActivitiBPMGateway;
import com.antheminc.oss.nimbus.domain.bpm.activiti.ActivitiExpressionManager;
import com.antheminc.oss.nimbus.domain.bpm.activiti.CommandExecutorTaskDelegate;
import com.antheminc.oss.nimbus.domain.cmd.exec.FunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultParamFunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.nav.DefaultActionNewInitEntityFunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.nav.PageIdEchoNavHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.process.AddFunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.process.EvalFunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.process.SetByRuleFunctionalHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.process.SetFunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.process.StatelessBPMFunctionHanlder;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.process.UpdateFunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.search.DefaultSearchFunctionHandlerExample;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.search.DefaultSearchFunctionHandlerLookup;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.search.DefaultSearchFunctionHandlerQuery;
import com.antheminc.oss.nimbus.support.expr.ExpressionEvaluator;
import com.antheminc.oss.nimbus.support.expr.SpelExpressionEvaluator;

/**
 * @author Sandeep Mantha
 *
 */
@Configuration 
public class DefaultProcessConfig {
	
	@Value("${process.supportStatefulProcesses:#{true}}")
	private Boolean supportStatefulProcesses;	
		
	@Bean
	public ActivitiExpressionManager activitiExpressionManager(){
		return new ActivitiExpressionManager();
	}
	
	@Bean
	public BPMGateway bpmGateway(BeanResolverStrategy beanResolver){
		return new ActivitiBPMGateway(beanResolver,supportStatefulProcesses);
	}		
	
	@Bean(name="default._new$execute?fn=_initEntity")
	public FunctionHandler<?, ?> defaultActionNewInitFunctionHandler(BeanResolverStrategy beanResolver){
		return new DefaultActionNewInitEntityFunctionHandler<>(beanResolver);
	}
	
	@Bean(name="default._get$execute?fn=param")
	public FunctionHandler<?, ?> defaultParamFunctionHandler(BeanResolverStrategy beanResolver){
		return new DefaultParamFunctionHandler<>(beanResolver);
	}
	
	@Bean(name="default._nav$execute?fn=default")
	public PageIdEchoNavHandler<?> pageIdEchoNavHandler(){
		return new PageIdEchoNavHandler<>();
	}
	
	@Bean(name="default._process$execute?fn=_set")
	public SetFunctionHandler<?,?> setFunctionHandler(){
		return new SetFunctionHandler<>();
	}
	
	@Bean(name="default._process$execute?fn=_update")
	public UpdateFunctionHandler<?,?> updateFunctionHandler(){
		return new UpdateFunctionHandler<>();
	}
	
	@Bean(name="default._process$execute?fn=_setByRule")
	public FunctionHandler<?,?> setByRuleFunctionHandler(){
		return new SetByRuleFunctionalHandler<>();
	}
	
	@Bean(name="default._process$execute?fn=_add")
	public AddFunctionHandler<?,?> addFunctionHandler(){
		return new AddFunctionHandler<>();
	}
	
	@Bean(name="default._process$execute?fn=_bpm")
	public StatelessBPMFunctionHanlder<?,?> statelessBPMFunctionHanlder(BeanResolverStrategy beanResolver){
		return new StatelessBPMFunctionHanlder<>(beanResolver);
	}	
	
	@Bean(name="expressionEvaluator")
	public ExpressionEvaluator expressionEvaluator(BeanResolverStrategy beanResolver){
		return new SpelExpressionEvaluator();
	}	
	
	@Bean(name="commandExecutorTaskDelegate")
	public CommandExecutorTaskDelegate commandExecutorTaskDelegate(BeanResolverStrategy beanResolver){
		return new CommandExecutorTaskDelegate(beanResolver);
	}	
	
	@Bean(name="default._search$execute?fn=lookup")
	public FunctionHandler<?, ?> lookupFunctionHandler(BeanResolverStrategy beanResolver){
		return new DefaultSearchFunctionHandlerLookup<>(beanResolver);
	}
	
	@Bean(name="default._search$execute?fn=example")
	public FunctionHandler<?, ?> exampleFunctionHandler(){
		return new DefaultSearchFunctionHandlerExample<>();
	}
	
	@Bean(name="default._search$execute?fn=query")
	public FunctionHandler<?, ?> queryFunctionHandler(){
		return new DefaultSearchFunctionHandlerQuery<>();
	}

	@Bean(name="default._process$execute?fn=_eval")
	public EvalFunctionHandler<?,?> evalFunctionHandler(ExpressionManager expressionManager){
		return new EvalFunctionHandler(expressionManager);
	}

}
