/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import com.anthem.nimbus.platform.spec.model.command.ValidationException;
import com.anthem.nimbus.platform.spec.model.dsl.Action;

/**
 * @author Soham Chakravarti
 *
 */
public interface State<T> {

	public T getState();
	
	public Action setState(T state);
	
	public void validateAndSetState(T state) throws ValidationException;
}
