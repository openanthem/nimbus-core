/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity.person;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import com.antheminc.oss.nimbus.core.entity.AbstractEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true)
public abstract class Phone<ID extends Serializable> extends AbstractEntity<ID> {
	
	private static final long serialVersionUID = 1L;


	public enum Type {
		CELL,
		OFFICE,
		HOME,
		FAX;
	}
	
	

	public static class IdLong extends Phone<Long> {
		
		private static final long serialVersionUID = 1L;
		
		
		@Id @Getter @Setter(value=AccessLevel.PROTECTED) 
		private Long id;
	}
	
	

	public static class IdString extends Phone<String> {
		
		private static final long serialVersionUID = 1L;
		
		
		@Id @Getter @Setter(value=AccessLevel.PROTECTED) 
		private String id;
	}


	
	private Type type;

	private String number;
	
	private String extension;
	
}
