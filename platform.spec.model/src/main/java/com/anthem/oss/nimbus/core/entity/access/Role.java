/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.access;

import java.time.LocalDate;
import java.util.Set;

import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true)
public abstract class Role<E extends Role.Entry<T>, T extends AccessEntity> 
extends AbstractEntity.IdLong {

	private static final long serialVersionUID = 1L;


	
	@Getter @Setter
	public static abstract class Entry<T extends AccessEntity> extends AbstractEntity.IdLong 
	implements AccessEntity {

		private static final long serialVersionUID = 1L;
		
        private Set<Permission> grantedPermissions;
		

		public Entry() {}
		
		public Entry(T referredAccess, Set<Permission> grantedPermissions) {
			verifyGrantedPermissions(grantedPermissions);
			setReferredAccess(referredAccess);
			this.grantedPermissions = grantedPermissions;
		}
		

		public abstract T getReferredAccess();
		
		public abstract void setReferredAccess(T referredAccess);
		
		
		@Override
		public Set<Permission> getPermissions() {
			return getGrantedPermissions();
		}
		
		public void verifyGrantedPermissions(Set<Permission> grantedPermissions) {
			
		}
	}
	

	private String code;

	private String name;
	
	private LocalDate effectiveDate;
	
	private LocalDate terminationDate;
	
	private String description;
	
	
	public abstract Set<E> getEntries();

	public abstract void setEntries(Set<E> entries);
	
}
