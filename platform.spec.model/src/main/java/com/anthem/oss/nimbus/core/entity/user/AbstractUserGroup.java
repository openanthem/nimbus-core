package com.anthem.oss.nimbus.core.entity.user;

import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Getter @Setter
public abstract class AbstractUserGroup extends AbstractEntity.IdLong {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String displayName;
	private String description;
	private Status status;
	
	
	public enum Status {
		ACTIVE,
		INACTIVE
	}

	
}
