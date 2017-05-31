package com.anthem.oss.nimbus.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anthem.oss.nimbus.core.bpm.DefaultExpressionHelper;
import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiExpressionManager;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.nav.PageIdEchoNavHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.process.AddFunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.process.SetFunctionHandler;
import com.anthem.oss.nimbus.core.domain.command.execution.search.DefaultSearchFunctionHandlerExample;
import com.anthem.oss.nimbus.core.domain.command.execution.search.DefaultSearchFunctionHandlerLookup;
import com.anthem.oss.nimbus.core.domain.command.execution.search.DefaultSearchFunctionHandlerQuery;

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
	
	@Bean(name="defaultExpressionHelper")
	public DefaultExpressionHelper defaultExpressionHelper(CommandMessageConverter converter){
		return new DefaultExpressionHelper(converter);
	}
	
	@Bean(name="default._nav$execute?fn=default")
	public PageIdEchoNavHandler<?> pageIdEchoNavHandler(){
		return new PageIdEchoNavHandler<>();
	}
	
	@Bean(name="default._process$execute?fn=_set")
	public SetFunctionHandler<?,?> setFunctionHandler(){
		return new SetFunctionHandler<>();
	}
	
	@Bean(name="default._process$execute?fn=_add")
	public AddFunctionHandler<?,?> addFunctionHandler(){
		return new AddFunctionHandler<>();
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
	
}
