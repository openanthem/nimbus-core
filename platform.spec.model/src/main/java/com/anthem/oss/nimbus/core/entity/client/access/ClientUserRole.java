/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.client.access;

import java.util.Set;

import com.anthem.oss.nimbus.core.domain.definition.ConfigNature.Ignore;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.access.Permission;
import com.anthem.oss.nimbus.core.entity.access.Role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */

@Domain(value="userrole", includeListeners={ListenerType.persistence})
@Repo(Database.rep_mongodb)
@Getter @Setter @ToString(callSuper=true)
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
	private String clientId;
	
	private String status;
	
	private String roleType; // standard/customized
	
	
}
