/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
@RequiredArgsConstructor
public class CollectionParamState implements InvocationHandler {

	private final Collection<?> target;

	private final Collection<?> proxy = (Collection<?>) Proxy.newProxyInstance(this.getClass().getClassLoader(),
			new Class[] { Collection.class }, this);

	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(getTarget(), args);
	}

}
