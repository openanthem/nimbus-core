package com.anthem.oss.nimbus.core.web;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 
 * @author Tony Lopez (AF42192)
 *
 */
//TODO: Hack solution, to be revisited. Objective is to set the threadContextInheritable property of DispatcherServlet
@Configuration
public class WebBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		 if (bean instanceof DispatcherServlet) {
             ((DispatcherServlet) bean).setThreadContextInheritable(true);
         }		
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	


}
