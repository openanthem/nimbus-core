/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.entity.user;


import com.antheminc.oss.nimbus.entity.access.Role;
import com.antheminc.oss.nimbus.entity.person.Address;
import com.antheminc.oss.nimbus.entity.person.Name;
import com.antheminc.oss.nimbus.entity.person.Person;
import com.antheminc.oss.nimbus.entity.person.Phone;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true)
public abstract class AbstractUser<R extends Role> extends Person<Address, Phone, Name> {
	
	private static final long serialVersionUID = 1L;
	
	private String loginId;
	
	private String location;	//TODO Make it Java TimeZone after modifying mongo-init inserts
	
}