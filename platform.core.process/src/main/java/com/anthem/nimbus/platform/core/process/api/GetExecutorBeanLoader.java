package com.anthem.nimbus.platform.core.process.api;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import com.anthem.nimbus.platform.core.process.api.exec.ClientActionExecutorGet;

/**
 * Created by AF13233 on 9/8/16.
 */

@Configuration
public class GetExecutorBeanLoader implements ApplicationContextAware {
    
	private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;
        generateVelocityTemplateBeans(genericApplicationContext);

    }


    private void generateVelocityTemplateBeans(GenericApplicationContext genericApplicationContext) throws BeansException {
       // BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ClientActionExecutorGet.class).setLazyInit(true);
       // genericApplicationContext.registerBeanDefinition(ClientActionExecutorGet.class.getName(), builder.getBeanDefinition());
        
    }

}
