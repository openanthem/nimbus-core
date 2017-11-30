/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.access;

import java.time.LocalDate;
import java.util.List;

import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rakesh Patel
 *
 */
@Getter @Setter
@ToString(callSuper=true)
public abstract class Role extends IdString {

	private static final long serialVersionUID = 1L;

	private String code;

	private String name;
	
	private LocalDate effectiveDate;
	
	private LocalDate terminationDate;
	
	private String description;
	
	private List<String> accessEntities;
	
	
}
