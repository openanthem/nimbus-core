/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import com.anthem.nimbus.platform.spec.model.command.ValidationException;

/**
 * @author Soham Chakravarti
 *
 */
public interface State<T> {

	/**
	 * 
	 * @return
	 */
	public T getState();
	
	/**
	 * 
	 * @param state
	 */
	public void setState(T state);
	
	/**
	 * 
	 * @param state
	 * @throws ValidationException
	 */
	public void validateAndSetState(T state) throws ValidationException;
	
}
