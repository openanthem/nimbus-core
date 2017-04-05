/**
 * 
 */
package com.anthem.oss.nimbus.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Jayant Chaudhuri
 *
 */
public class ProcessBeanResolver implements ApplicationContextAware {
	
	public static ApplicationContext appContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}
	
	public static Object getBean(String beanName) {
		return appContext.getBean(beanName);
	}
	
	public static <T> T getBean(String beanName, Class<T> beanType) {
		return appContext.getBean(beanName,beanType);
	}
	
	public static Object getBean(Class<?> beanType) {
		return appContext.getBean(beanType);
	}
}
