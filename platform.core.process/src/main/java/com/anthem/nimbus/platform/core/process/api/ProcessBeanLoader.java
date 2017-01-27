package com.anthem.nimbus.platform.core.process.api;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author Sandeep Mantha
 *
 */

@Configuration
public class ProcessBeanLoader implements ApplicationContextAware {

	@Autowired
	RepositoryService repositoryService;
	
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		GenericApplicationContext appContext = (GenericApplicationContext)applicationContext;
		injectProcessBean(appContext);
	}
	
	private void injectProcessBean(GenericApplicationContext appContext){
    	
    	List<ProcessDefinition> deployedProcessDefList = repositoryService.createProcessDefinitionQuery().active().list();
    	deployedProcessDefList.forEach(processDefinition -> {
	    	BeanDefinition def = BeanDefinitionBuilder.genericBeanDefinition(ProcessBean.class).getBeanDefinition();
	    	def.getPropertyValues().add("processId", processDefinition.getName());
    		appContext.registerBeanDefinition(processDefinition.getKey(), def);
	   });
    }


}
