/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state;

import com.antheminc.oss.nimbus.core.domain.command.Action;

/**
 * @author Soham Chakravarti
 *
 */
public interface State<T> {

	public T getState();
	
	public Action setState(T state);
	
	//public void validateAndSetState(T state) throws ValidationException;
}
