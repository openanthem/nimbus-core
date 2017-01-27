/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.access;

import java.util.Set;

/**
 * @author Soham Chakravarti
 *
 */
public interface AccessEntity {

	/**
	 * 
	 * @return
	 */
	public Set<Permission> getPermissions();
	
}
