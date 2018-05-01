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

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang.reflect.FieldUtils;
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
			Field f = FieldUtils.getField(beanClass, fieldName, true);
			boolean b = f.getType().equals(boolean.class);
			
			String getterName = (b ? "is" : "get") + StringUtils.capitalize(fieldName);
			String setterName = "set" + StringUtils.capitalize(fieldName);
			
			Method getter = makeAccessible(BeanUtils.findMethod(beanClass, getterName));
			Method setter = makeAccessible(BeanUtils.findMethod(beanClass, setterName, f.getType()));
			
		
			MethodHandles.Lookup lookup = MethodHandles.lookup();
			
			MethodHandle get = constructGetHandle(lookup, getter);
			MethodHandle set = constructSetHandle(lookup, setter);
			
			return new ValueAccessor(getter, setter, get, set);
			
		} catch (Throwable t) {
			throw new InvalidStateException("POJO construct MethodHandles on beanClass: "+beanClass
					+ " for fieldName: "+fieldName, 
					t);
		}
	}
	
	private static Method makeAccessible(Method m) {
		if(m==null || m.isAccessible())
			return m;
		
		m.setAccessible(true);
		return m;
	}
	
	public static MethodHandle constructGetHandle(MethodHandles.Lookup lookup, Method m) throws Throwable {
		if(m==null)
			return null;
		
		MethodHandle get = lookup.unreflect(m);
		return LambdaMetafactory.metafactory(
				lookup, 
				"apply", 
				MethodType.methodType(Function.class),
				MethodType.methodType(Object.class, Object.class), 
				get, 
				get.type()).getTarget();
	}
	
	public static MethodHandle constructSetHandle(MethodHandles.Lookup lookup, Method m) throws Throwable {
		if(m==null)
			return null;
		
		MethodHandle set = lookup.unreflect(m);
		return LambdaMetafactory.metafactory(
				lookup, 
				"accept", 
				MethodType.methodType(BiConsumer.class), 
				MethodType.methodType(Void.TYPE, Object.class, Object.class),
				set, 
				set.type()).getTarget();
	}
	
	public static MethodHandle constructInitHandle(MethodHandles.Lookup lookup, Class<?> declaringClass) throws Throwable {
		MethodHandle init = lookup.findConstructor(declaringClass, MethodType.methodType(declaringClass));

		return LambdaMetafactory.metafactory(
				lookup, 
				"get", 
				MethodType.methodType(Supplier.class),
				MethodType.methodType(Object.class), 
				init, 
				init.type()).getTarget();
	}
	

}
