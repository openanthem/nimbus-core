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
import java.util.List;

import org.springframework.data.annotation.Id;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="person")
@Getter @Setter @ToString(callSuper=true)
public abstract class Person<ID extends Serializable, A extends Address<ID>, P extends Phone<ID>, N extends Name<ID>>
		extends AbstractEntity<ID> {
	
	private static final long serialVersionUID = 1L;

	
	
	public static class IdLong extends Person<Long, Address.IdLong, Phone.IdLong, Name.IdLong> {
		
		private static final long serialVersionUID = 1L;

		
		@Id @Getter @Setter
		private Long id;
	}
	

	
	public static class IdString extends Person<String, Address.IdString, Phone.IdString, Name.IdString> {
		
		private static final long serialVersionUID = 1L;

		
		@Id @Getter @Setter 
		private String id;
	}
	

	
	private Name.IdString name;

	private List<Address.IdString> addresses;

	private String email;
	
	private String displayName;

	private List<Phone.IdString> contactPhones;
	
}
