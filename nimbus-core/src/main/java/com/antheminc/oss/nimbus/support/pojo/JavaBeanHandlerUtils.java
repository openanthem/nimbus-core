/**
 *  Copyright 2016-2018 the original author or authors.
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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.antheminc.oss.nimbus.domain.model.state.EntityState.ValueAccessor;
import com.antheminc.oss.nimbus.domain.model.state.InvalidStateException;

/**
 * @author Soham Chakravarti
 *
 */
public final class JavaBeanHandlerUtils {
	
	public static ValueAccessor constructValueAccessor(Class<?> beanClass, String fieldName) {
		try {
			Method readMethod = getReadMethod(beanClass, fieldName);
			Method writeMethod = getWriteMethod(beanClass, fieldName);
			
			PropertyDescriptor pd = new PropertyDescriptor(fieldName, readMethod, writeMethod);
			return new ValueAccessor(pd);
		} catch (Exception ex) {
			throw new InvalidStateException("POJO construct MethodHandles on beanClass: "+beanClass
					+ " for fieldName: "+fieldName, 
					ex);
		}
	}
	
	private static Method getReadMethod(Class<?> beanClass, String fieldName) {
		Field f = FieldUtils.getField(beanClass, fieldName, true);
		boolean b = f.getType().equals(boolean.class);
		
		String getterName = (b ? "is" : "get") + StringUtils.capitalize(fieldName);
		Method getter = makeAccessible(BeanUtils.findMethod(beanClass, getterName));
		return getter;
	}
	
	private static Method getWriteMethod(Class<?> beanClass, String fieldName) {
		Field f = FieldUtils.getField(beanClass, fieldName, true);
		String setterName = "set" + StringUtils.capitalize(fieldName);
		
		Method setter = makeAccessible(BeanUtils.findMethod(beanClass, setterName, f.getType()));
		return setter;
	}
	
	private static Method makeAccessible(Method m) {
		if(m==null || m.isAccessible())
			return m;
		
		m.setAccessible(true);
		return m;
	}

}
