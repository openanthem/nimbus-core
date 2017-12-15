/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity.person;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="name")
@Getter @Setter @ToString(callSuper=true)
public abstract class Name<ID extends Serializable> extends AbstractEntity<ID> {
	
	private static final long serialVersionUID = 1L;
	
	
	
	public static class IdLong extends Name<Long> {
		
		private static final long serialVersionUID = 1L;

		@Id @Getter @Setter(value=AccessLevel.PROTECTED) 
		private Long id;
	}
	
	
	
	public static class IdString extends Name<String> {
		
		private static final long serialVersionUID = 1L;
		
		@Id @Getter @Setter(value=AccessLevel.PROTECTED) 
		private String id;
	}
	

	private String firstName;

	private String lastName;

	private String middleName;
	
	private String fullName;
	
	public String getFullName() {
		return firstName + ' ' + lastName;		
	}
	
}
