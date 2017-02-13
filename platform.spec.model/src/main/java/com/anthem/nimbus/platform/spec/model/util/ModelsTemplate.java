/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.util;

import com.anthem.oss.nimbus.core.domain.InvalidConfigException;

/**
 * @author Soham Chakravarti
 *
 */
public class ModelsTemplate {

	
	/**
	 * 
	 * @param classNm
	 * @return
	 */
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
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} 
		catch (Exception ex) {
			throw new InvalidConfigException("Class could not be instantiated with blank constructor: " + clazz, ex);
		}
	}
	
}
