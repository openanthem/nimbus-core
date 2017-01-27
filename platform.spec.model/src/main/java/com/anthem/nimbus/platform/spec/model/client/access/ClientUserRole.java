/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.client.access;

import java.util.Set;

import com.anthem.nimbus.platform.spec.model.access.Permission;
import com.anthem.nimbus.platform.spec.model.access.Role;
import com.anthem.nimbus.platform.spec.model.dsl.ConfigNature.Ignore;
import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@CoreDomain(value="userrole")
@Setter @Getter
public class ClientUserRole extends Role<ClientUserRole.Entry, ClientAccessEntity> {

	private static final long serialVersionUID = 1L;
	
	
	
	@Setter @Getter
	public static class Entry extends Role.Entry<ClientAccessEntity> {

		private static final long serialVersionUID = 1L;
		
		@Ignore
		private ClientAccessEntity referredAccess;
		
		
		public Entry() { }
		
		public Entry(ClientAccessEntity referredSubject, Set<Permission> grantedPermissions) {
			super(referredSubject, grantedPermissions);
		}
	}
	
	private Set<ClientUserRole.Entry> entries;
	
	//TODO figure out if this can stay as just id or needs to be converted to object reference
	private Long clientId;
	
}
