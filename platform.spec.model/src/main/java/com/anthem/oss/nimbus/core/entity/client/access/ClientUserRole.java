/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.client.access;


import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.access.Role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value="userrole", includeListeners={ListenerType.persistence})
@Repo(Database.rep_mongodb)
@Getter @Setter @ToString(callSuper=true)
public class ClientUserRole extends Role {

	private static final long serialVersionUID = 1L;
    
    private String clientId;

    private boolean allowInheritance;
    private String status;
    private RoleType roleType;
    private String roleCategory;
    private String displayName;

	public enum Status {
		ACTIVE,
		INACTIVE
	}
    
    public enum RoleType {
		STANDARD,
		CUSTOMIZED
	}
	
	
}
