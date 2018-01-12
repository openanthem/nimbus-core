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
/**
 * 
 */
package com.anthem.oss.nimbus.core.utils;

import java.lang.reflect.Method;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.util.JustLogit;

/**
 * @author Soham Chakravarti
 *
 */
public class JavaBeanHandlerReflection implements JavaBeanHandler {

	private JustLogit logit = new JustLogit(getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValue(Method readMethod, Object target) {
		try {
			return (target == null) ? null : (T)readMethod.invoke(target);
		}
		catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to execute read on : "+readMethod+" with target "+target, ex);
		}
	}
	
	@Override
	public <T> void setValue(Method writeMethod, Object target, T value) {
		logit.trace(()->"setValue@enter -> writeMethod: "+writeMethod+" target: "+target+" value: "+value);
		
		try {
			writeMethod.invoke(target, value);
		} catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to execute write on : "+writeMethod+" with value: "+value, ex);
		}
		logit.trace(()->"setValue@exit");
	}
	
	@Override
	public <T> T instantiate(Class<T> clazz) {
		logit.trace(()->"instantiate@enter -> clazz: "+clazz);
		
		final T newInstance;
		try {
			newInstance = clazz.newInstance();
		} 
		catch (Exception ex) {
			throw new InvalidConfigException("Class could not be instantiated with blank constructor: " + clazz, ex);
		}
		
		logit.trace(()->"instantiate@exit");		
		return newInstance;
	}
}
