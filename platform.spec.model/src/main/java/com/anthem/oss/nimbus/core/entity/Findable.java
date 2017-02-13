/**
 * 
 */
package com.anthem.oss.nimbus.core.entity;

/**
 * @author Soham Chakravarti
 *
 */
public interface Findable<T> {

	public boolean isFound(T by);
	
}
