/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.access;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Setter @Getter
public class DefaultRole extends Role<DefaultRole.Entry, DefaultAccessEntity> {
	
	private static final long serialVersionUID = 1L;


	
	@Setter @Getter
	public static class Entry extends Role.Entry<DefaultAccessEntity> {
		
		private static final long serialVersionUID = 1L;
		
        private DefaultAccessEntity referredAccess;
		

		public Entry() { }
		
		public Entry(DefaultAccessEntity referredSubject, Set<Permission> grantedPermissions) {
			super(referredSubject, grantedPermissions);
		}
	}
	
	
	private Set<DefaultRole.Entry> entries;
	
}
