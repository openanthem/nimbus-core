/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity.access;

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
