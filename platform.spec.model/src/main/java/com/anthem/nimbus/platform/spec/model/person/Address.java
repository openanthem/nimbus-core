/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.person;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import com.anthem.nimbus.platform.spec.model.AbstractModel;
import com.anthem.nimbus.platform.spec.model.dsl.Model;
import com.anthem.nimbus.platform.spec.model.person.Values;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true)
public abstract class Address<ID extends Serializable> extends AbstractModel<ID> {

	private static final long serialVersionUID = 1L;

	
	public enum Type {
		MAILING,
		BILLING;
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
	@Model.Param.Values(Values.AddressType.class)
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
	@Model.Param.Values(Values.Country.class)
	private String countryCd;
	
}
