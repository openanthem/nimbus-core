package com.anthem.oss.nimbus.core.config;

import org.activiti.engine.impl.el.ExpressionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.bpm.DefaultExpressionHelper;
import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiExpressionManager;
import com.anthem.oss.nimbus.core.domain.command.execution.nav.PageIdEchoNavHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.process.AddFunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.process.EvalFunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.process.SetFunctionHandler;

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
	
	@Bean(name="default._nav$execute?fn=default")
	public PageIdEchoNavHandler<?> pageIdEchoNavHandler(){
		return new PageIdEchoNavHandler();
	}
	
	@Bean(name="default._process$execute?fn=_set")
	public SetFunctionHandler<?,?> setFunctionHandler(){
		return new SetFunctionHandler();
	}
	
	@Bean(name="default._process$execute?fn=_add")
	public AddFunctionHandler<?,?> addFunctionHandler(){
		return new AddFunctionHandler();
	}	

	@Bean(name="default._process$execute?fn=_eval")
	public EvalFunctionHandler<?,?> evalFunctionHandler(ExpressionManager expressionManager){
		return new EvalFunctionHandler(expressionManager);
	}
}
