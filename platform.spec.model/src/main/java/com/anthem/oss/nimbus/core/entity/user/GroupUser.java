/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.user;

import org.springframework.data.annotation.Id;

import com.anthem.oss.nimbus.core.domain.definition.Model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Swetha Vemuri
 *
 */
@Model
@Getter @Setter
public class GroupUser {

	@Id
	private String userId;
	
	private boolean admin;
}
