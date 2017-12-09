package com.anthem.oss.nimbus.core.config;

import org.activiti.engine.impl.el.ExpressionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.bpm.BPMGateway;
import com.anthem.oss.nimbus.core.bpm.DefaultExpressionHelper;
import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiBPMGateway;
import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiExpressionManager;
import com.anthem.oss.nimbus.core.bpm.activiti.CommandExecutorTaskDelegate;
import com.anthem.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.fn.DefaultParamFunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.nav.DefaultActionNewInitEntityFunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.nav.PageIdEchoNavHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.process.AddCollectionsFunctionalHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.process.AddFunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.process.EvalFunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.process.SetByRuleFunctionalHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.process.SetFunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.process.StatelessBPMFunctionHanlder;
import com.anthem.oss.nimbus.core.domain.command.execution.process.UpdateFunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.search.DefaultSearchFunctionHandlerExample;
import com.anthem.oss.nimbus.core.domain.command.execution.search.DefaultSearchFunctionHandlerLookup;
import com.anthem.oss.nimbus.core.domain.command.execution.search.DefaultSearchFunctionHandlerQuery;
import com.anthem.oss.nimbus.core.domain.expr.ExpressionEvaluator;
import com.anthem.oss.nimbus.core.domain.expr.SpelExpressionEvaluator;

/**
 * @author Sandeep Mantha
 *
 */
@Configuration 
public class DefaultProcessConfig {
	
	@Bean
	public ActivitiExpressionManager activitiExpressionManager(){
		return new ActivitiExpressionManager();
	}
	
	@Bean
	public DefaultExpressionHelper defaultExpressionHelper(BeanResolverStrategy beanResolver){
		return new DefaultExpressionHelper(beanResolver);
	}
	
	@Bean
	public BPMGateway bpmGateway(BeanResolverStrategy beanResolver){
		return new ActivitiBPMGateway(beanResolver);
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
	public FunctionHandler<?, ?> lookupFunctionHandler(){
		return new DefaultSearchFunctionHandlerLookup<>();
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
	
	@Bean(name="default._process$execute?fn=_addCollection")
	public AddCollectionsFunctionalHandler<?,?> setAddCollectionsFunctionalHandler(BeanResolverStrategy beanResolver){
		return new AddCollectionsFunctionalHandler<>(beanResolver);
	}

}
