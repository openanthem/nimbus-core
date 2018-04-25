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

import org.apache.commons.lang3.ClassUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;

/**
 * @author Soham Chakravarti
 *
 */
public class ClassLoadUtils {

	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> loadClass(String classNm) {
		try {
			return (Class<T>)ClassUtils.getClass(classNm, false);
			//return (Class<T>)Class.forName(classNm);
		} 
		catch (ClassNotFoundException ex) {
			throw new InvalidConfigException("Anomaly as this state should not occur "
					+ "given that the class-name used to load class comes from package scanning: " + classNm, ex);
		}
	}
	
	
	public static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new InvalidConfigException("The class does not have a default constructor or not instantiable (like it is an interface or abstract class): " + clazz.getName(), e);
		} catch (IllegalAccessException e) {
			throw new InvalidConfigException("The class is not accessible (is the constructor private ?): " + clazz.getName(), e);
		}
	}
	
	
}
