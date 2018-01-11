/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity.user;

import java.util.List;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;

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
	
	//TODO delete this attribute once the vgusergroup.drl is triggered on setState to set the count
	private Integer memberCount;
}
