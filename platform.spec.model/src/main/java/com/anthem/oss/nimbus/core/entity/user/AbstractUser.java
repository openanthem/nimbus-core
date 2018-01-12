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
package com.anthem.oss.nimbus.core.entity.user;


import org.springframework.data.annotation.Id;

import com.anthem.oss.nimbus.core.entity.access.Role;
import com.anthem.oss.nimbus.core.entity.person.Address;
import com.anthem.oss.nimbus.core.entity.person.Name;
import com.anthem.oss.nimbus.core.entity.person.Person;
import com.anthem.oss.nimbus.core.entity.person.Phone;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true)
public abstract class AbstractUser<R extends Role> extends Person<String, Address.IdString, Phone.IdString, Name.IdString> {
	
	private static final long serialVersionUID = 1L;
	
	@Id 
	private String id;
	
	private String loginId;
	
	private String location;	//TODO Make it Java TimeZone after modifying mongo-init inserts
	
}