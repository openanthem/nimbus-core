/**
 * 
 */
package com.anthem.nimbus.platform.spec.model;

/**
 * @author Soham Chakravarti
 *
 */
public interface Findable<T> {

	public boolean isFound(T by);
	
}
