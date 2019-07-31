/**
 *  Copyright 2016-2019 the original author or authors.
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

import com.antheminc.oss.nimbus.domain.cmd.exec.internal.process.ActivitiBPMProcessHandler;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Sandeep Mantha
 *
 */
@EnableLoggingInterceptor
@Configuration
@Getter(value = AccessLevel.PROTECTED)
public class ActivitiProcessAsBeanRegistrar implements ApplicationContextAware {

	@Autowired
	RepositoryService repositoryService;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		GenericApplicationContext appContext = (GenericApplicationContext)applicationContext;
		injectProcessBean(appContext);
	}
	
	private void injectProcessBean(GenericApplicationContext appContext){
    	
    	List<ProcessDefinition> deployedProcessDefList = getRepositoryService().createProcessDefinitionQuery().active().list();
    	deployedProcessDefList.forEach(processDefinition -> {
	    	BeanDefinition def = BeanDefinitionBuilder.genericBeanDefinition(ActivitiBPMProcessHandler.class).getBeanDefinition();
	    	def.getPropertyValues().add("processId", processDefinition.getName());
    		appContext.registerBeanDefinition(processDefinition.getKey(), def);
	   });
    }


}
