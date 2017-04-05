/**
 * 
 */
package com.anthem.oss.nimbus.core.rules;

import java.lang.reflect.Field;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.definition.Model;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultRulesEngineFactoryProducer implements RulesEngineFactoryProducer, ApplicationContextAware {

	private ApplicationContext ctx;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}
	
	@Override
	public RulesEngineFactory getFactory(Class<?> model) {
		Model modelDefinition = AnnotationUtils.findAnnotation(model, Model.class);
		if(modelDefinition==null) return null;
		
		String beanName = resolveBeanName(modelDefinition);
		if(!ctx.containsBean(beanName)) return null;
		
		RulesEngineFactory factory = ctx.getBean(beanName, RulesEngineFactory.class);
		return factory;
	}
	
	protected String resolveBeanName(Model modelDefinition) {
		return "rules.factory." + modelDefinition.rules();
	}
	
	@Override
	public RulesEngineFactory getFactory(Field param) {
		throw new UnsupportedOperationException("Param level rules processing is not yet supported.");
	}

}
