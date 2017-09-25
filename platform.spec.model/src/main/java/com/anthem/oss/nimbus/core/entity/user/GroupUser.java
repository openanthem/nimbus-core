/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.user;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import com.anthem.oss.nimbus.core.domain.definition.Model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Swetha Vemuri
 *
 */
@Model("groupuser")
@Getter @Setter
public class GroupUser {

	@Id
	private String userId;
	
	private boolean admin;
	
	private LocalDate effectiveDate;
	
	private LocalDate retireDate;
}
