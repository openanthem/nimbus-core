package com.anthem.oss.nimbus.core.config;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.anthem.oss.nimbus.core.bpm.DefaultExpressionHelper;
import com.anthem.oss.nimbus.core.bpm.activiti.ModelInstantiationServiceTaskDelegate;
import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiDAO;
import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiExpressionManager;
import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiGateway;
import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiGatewayHelper;
import com.anthem.oss.nimbus.core.bpm.activiti.SpELBasedTaskRouter;
import com.anthem.oss.nimbus.core.bpm.activiti.TaskCompletionListener;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessGateway;
import com.anthem.oss.nimbus.core.integration.sa.ProcessExecutionCtxHelper;

/**
 * @author Sandeep Mantha
 *
 */

@Configuration 
public class DefaultProcessConfig {
	
	@Bean
	@Scope(value = "prototype")
	public ModelInstantiationServiceTaskDelegate activitiAssignmentServiceTaskDelegate(ProcessGateway processGateway){
		return new ModelInstantiationServiceTaskDelegate(processGateway);
	}
	
	@Bean
	public ActivitiExpressionManager activitiExpressionManager(){
		return new ActivitiExpressionManager();
	}
	
	@Bean
	public ActivitiGateway activitiGateway(RuntimeService runtimeService, ProcessExecutionCtxHelper processExecutionCtx,
			TaskService taskService, ActivitiDAO activitiDAO,
			ProcessGateway processGateway){
		return new ActivitiGateway(runtimeService,processExecutionCtx,taskService,activitiDAO,processGateway);
	}
	
	
	@Bean
	@Scope(value = "prototype")
	public ActivitiGatewayHelper activitiGatewayHelper(){
		return new ActivitiGatewayHelper();
	}
	//name is redundant
	@Bean(name="spELBasedTaskRouter")
	public SpELBasedTaskRouter spELBasedTaskRouter(){
		return new SpELBasedTaskRouter();
	}
	
	@Bean
	public TaskCompletionListener taskCompletionListener(TaskService taskService){
		return new TaskCompletionListener(taskService);
	}
	
	@Bean(name="defaultExpressionHelper")
	public DefaultExpressionHelper defaultExpressionHelper(CommandMessageConverter converter){
		return new DefaultExpressionHelper(converter);
	}
	
}
