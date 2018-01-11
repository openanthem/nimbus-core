package com.antheminc.oss.nimbus.core.entity.user;

import com.antheminc.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Getter @Setter
public abstract class AbstractUserGroup extends AbstractEntity.IdString {

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String displayName;
	
	private String description;
	
	private boolean admin;
	
	//@NotNull
	//@Model.Param.Values(url="staticCodeValue-/orgStatus")
	private String status;
	
	public enum Status {
		ACTIVE,
		INACTIVE
	}
}
