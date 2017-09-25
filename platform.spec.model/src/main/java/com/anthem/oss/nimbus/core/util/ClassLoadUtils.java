/**
 * 
 */
package com.anthem.oss.nimbus.core.util;

import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;

/**
 * @author Soham Chakravarti
 *
 */
public class ClassLoadUtils {

	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> loadClass(String classNm) {
		try {
			return (Class<T>)Class.forName(classNm);
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
