/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity.user;

import java.time.LocalDate;

import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Swetha Vemuri
 *
 */
@Model("groupuser")
@Getter @Setter
public class GroupUser extends IdString {

	private static final long serialVersionUID = 1L;

	private String userId;
	
	private boolean admin;
	
	private LocalDate effectiveDate;
	
	private boolean retired;
	
	private LocalDate retireDate;
}
