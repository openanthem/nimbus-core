/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.entity.person;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.entity.AbstractEntity;

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
	
	private String street3;

	@NotNull
	private String city;
	
	private String county;

	@NotNull
	private String zip;

	private String zipExtn;

	@NotNull
	private String stateCd;

	@NotNull
	//@Model.Param.Values(url="staticCodeValue-/country")
	private String countryCd;
	
}
