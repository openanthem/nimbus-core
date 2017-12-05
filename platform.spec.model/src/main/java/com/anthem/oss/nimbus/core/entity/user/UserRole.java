package com.antheminc.oss.nimbus.core.entity.user;

import java.time.LocalDate;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;

import lombok.Getter;
import lombok.Setter;
/**
 * 
 * @author Swetha Vemuri
 *
 */

@Domain("clientuserrole")
@Getter @Setter
public class UserRole {
	
	private String roleId;
	
	private LocalDate effectiveDate;
	private LocalDate terminationDate;
}
