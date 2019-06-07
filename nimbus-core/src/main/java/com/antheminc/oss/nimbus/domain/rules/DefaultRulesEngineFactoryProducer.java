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
package com.antheminc.oss.nimbus.domain.rules;

import java.lang.reflect.Field;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import com.antheminc.oss.nimbus.domain.defn.Model;

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
