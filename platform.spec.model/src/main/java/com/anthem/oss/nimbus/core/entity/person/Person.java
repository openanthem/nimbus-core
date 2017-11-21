/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.person;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;

import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true)
public abstract class Person<ID extends Serializable, A extends Address<ID>, P extends Phone<ID>, N extends Name<ID>>
		extends AbstractEntity<ID> {
	
	private static final long serialVersionUID = 1L;

	
	
	public static class IdLong extends Person<Long, Address.IdLong, Phone.IdLong, Name.IdLong> {
		
		private static final long serialVersionUID = 1L;

		
		@Id @Getter @Setter(value=AccessLevel.PROTECTED) 
		private Long id;
	}
	

	
	public static class IdString extends Person<String, Address.IdString, Phone.IdString, Name.IdString> {
		
		private static final long serialVersionUID = 1L;

		
		@Id @Getter @Setter(value=AccessLevel.PROTECTED) 
		private String id;
	}
	

	
	private Name.IdString name;

	private List<Address.IdString> addresses;

	private String email;
	
	private String displayName;

	private List<Phone.IdString> contactPhones;
	
}
