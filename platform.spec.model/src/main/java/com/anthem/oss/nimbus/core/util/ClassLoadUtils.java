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
	
	
}
