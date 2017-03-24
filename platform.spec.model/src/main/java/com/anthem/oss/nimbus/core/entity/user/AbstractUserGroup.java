/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.user;

import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author AC67870
 *
 */
@Getter @Setter
public class AbstractUserGroup extends AbstractEntity.IdLong {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String displayName;
	private String description;
	private Status status;
	
	
	enum Status {
		ACTIVE,
		INACTIVE
	}

	
}
