/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.access;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Setter @Getter
public class PlatformRole extends Role<PlatformRole.Entry, PlatformAccessEntity> {
	
	private static final long serialVersionUID = 1L;


	
	@Setter @Getter
	public static class Entry extends Role.Entry<PlatformAccessEntity> {
		
		private static final long serialVersionUID = 1L;
		
        private PlatformAccessEntity referredAccess;
		

		public Entry() { }
		
		public Entry(PlatformAccessEntity referredSubject, Set<Permission> grantedPermissions) {
			super(referredSubject, grantedPermissions);
		}
	}
	
	
	private Set<PlatformRole.Entry> entries;
	
}
