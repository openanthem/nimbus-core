/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity.client.access;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value="authorities", includeListeners={ListenerType.persistence})
@Repo(Database.rep_mongodb)
@Getter @Setter @ToString(callSuper=true)
public class ClientAccessEntity extends AbstractEntity.IdString {
	
	private static final long serialVersionUID = 1L;
	
	private String code; // case_management, member_management ...., 
	
}
