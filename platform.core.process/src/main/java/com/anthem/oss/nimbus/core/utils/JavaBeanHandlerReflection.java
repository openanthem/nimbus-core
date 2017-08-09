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
