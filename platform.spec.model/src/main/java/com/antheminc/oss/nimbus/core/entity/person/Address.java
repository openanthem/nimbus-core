/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity.person;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

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
//@Domain(value="address", includeListeners={ListenerType.persistence})
//@Repo(Database.rep_mongodb)
@Domain(value="address")
@Getter @Setter @ToString(callSuper=true)
public abstract class Address<ID extends Serializable> extends AbstractEntity<ID> {

	private static final long serialVersionUID = 1L;

	
	public enum Type {
		MAILING,
		BILLING,
		HOME,
		WORK,
		BUSINESS;
	}
	

	
	public static class IdLong extends Address<Long> {
		
		private static final long serialVersionUID = 1L;
		
		@Id @Getter @Setter(value=AccessLevel.PROTECTED) 
		private Long id;
	}
	

	
	public static class IdString extends Address<String> {
		
		private static final long serialVersionUID = 1L;
		
		@Id @Getter @Setter(value=AccessLevel.PROTECTED) 
		private String id;
	}
	
	

	@NotNull
	//@Model.Param.Values(url="Anthem/fep/icr/p/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/addressType')")
	private Type type;

	@NotNull
	private String street1;

	private String street2;

	@NotNull
	private String city;

	@NotNull
	private String zip;

	private String zipExtn;

	@NotNull
	private String stateCd;

	@NotNull
	//@Model.Param.Values(url="staticCodeValue-/country")
	private String countryCd;
	
}
