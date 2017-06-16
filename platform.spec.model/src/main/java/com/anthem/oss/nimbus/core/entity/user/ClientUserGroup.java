/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.user;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.client.ClientEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value="clientusergroup", includeListeners={ListenerType.persistence})
@Repo(Database.rep_mongodb)
@Getter @Setter @ToString(callSuper=true)
public class ClientUserGroup extends AbstractUserGroup {

	private static final long serialVersionUID = 1L;
	
	private String organizationId;
	
	private List<GroupUser> members;
	
	private String[] memberUserIds;
}
