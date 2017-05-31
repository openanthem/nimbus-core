package com.anthem.oss.nimbus.core.entity.queue;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value="groupmember", includeListeners={ListenerType.persistence})
@Repo
@Getter @Setter
public class MGroupMember extends IdString {

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String userName;
	
	private boolean isAdmin;

	
}
