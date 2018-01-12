/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
package com.anthem.oss.nimbus.core.config;

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

import com.anthem.oss.nimbus.core.domain.command.execution.process.ActivitiBPMProcessHandler;

/**
 * @author Sandeep Mantha
 *
 */

@Configuration
public class ActivitiProcessAsBeanRegistrar implements ApplicationContextAware {

	@Autowired
	RepositoryService repositoryService;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		GenericApplicationContext appContext = (GenericApplicationContext)applicationContext;
		injectProcessBean(appContext);
	}
	
	private void injectProcessBean(GenericApplicationContext appContext){
    	
    	List<ProcessDefinition> deployedProcessDefList = repositoryService.createProcessDefinitionQuery().active().list();
    	deployedProcessDefList.forEach(processDefinition -> {
	    	BeanDefinition def = BeanDefinitionBuilder.genericBeanDefinition(ActivitiBPMProcessHandler.class).getBeanDefinition();
	    	def.getPropertyValues().add("processId", processDefinition.getName());
    		appContext.registerBeanDefinition(processDefinition.getKey(), def);
	   });
    }


}
