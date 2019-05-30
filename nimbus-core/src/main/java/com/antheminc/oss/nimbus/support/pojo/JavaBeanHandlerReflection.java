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
package com.antheminc.oss.nimbus.support.pojo;

import java.lang.reflect.Method;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ValueAccessor;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * @author Soham Chakravarti
 *
 */
public class JavaBeanHandlerReflection implements JavaBeanHandler {

	private JustLogit logit = new JustLogit(getClass());
	
	@Override
	public <T> T getValue(ValueAccessor va, Object target) {
		return getValue(va.getReadMethod(), target);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(Method readMethod, Object target) {
		try {
			return (target == null) ? null : (T)readMethod.invoke(target);
		}
		catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to execute read on : "+readMethod+" with target "+target, ex);
		}
	}
	
	@Override
	public <T> void setValue(ValueAccessor va, Object target, T value) {
		setValue(va.getWriteMethod(), target, value);
	}
	
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
