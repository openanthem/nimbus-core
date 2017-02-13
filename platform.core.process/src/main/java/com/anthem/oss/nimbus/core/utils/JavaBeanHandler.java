/**
 * 
 */
package com.anthem.oss.nimbus.core.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * @author Soham Chakravarti
 *
 */
public interface JavaBeanHandler {

	default public <T> T getValue(PropertyDescriptor pd, Object target) {
		return getValue(pd.getReadMethod(), target);
	}
	
	public <T> T getValue(Method readMethod, Object target);	
	
	default public <T> void setValue(PropertyDescriptor pd, Object target, T value) {
		setValue(pd.getWriteMethod(), target, value);
	}
	public <T> void setValue(Method readMethod, Object target, T value);
	
	
	public <T> T instantiate(Class<T> clazz);
}
