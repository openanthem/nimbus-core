package com.anthem.oss.nimbus.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anthem.oss.nimbus.core.bpm.DefaultExpressionHelper;
import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiExpressionManager;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;

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
	
}
