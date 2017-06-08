package com.anthem.oss.nimbus.core.entity.user;

import javax.validation.constraints.NotNull;

import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;

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
	
	//@NotNull
	//@Model.Param.Values(url="staticCodeValue-/orgStatus")
	private Status status;
	
	public enum Status {
		ACTIVE,
		INACTIVE
	}
}
