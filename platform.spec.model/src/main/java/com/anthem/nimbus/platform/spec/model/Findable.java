/**
 * 
 */
package com.anthem.nimbus.platform.spec.model;

/**
 * @author Soham Chakravarti
 *
 */
public interface Findable<T> {

	/**
	 * 
	 * @param by
	 * @return
	 */
	public boolean isFound(T by);
	
}
