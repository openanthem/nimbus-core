package com.anthem.oss.nimbus.core.entity.user;

import java.time.LocalDate;

import com.anthem.oss.nimbus.core.domain.definition.Model;

import lombok.Getter;
import lombok.Setter;
/**
 * 
 * @author Swetha Vemuri
 *
 */

@Model("clientuserrole")
@Getter @Setter
public class UserRole {
	
	private String roleCode;
	
	private LocalDate retiredDate;
}
