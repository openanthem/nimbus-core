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
package com.antheminc.oss.nimbus.entity.client.user;

import java.util.List;

import org.springframework.data.annotation.ReadOnlyProperty;

import com.antheminc.oss.nimbus.domain.defn.ConfigNature.Ignore;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.client.Client;
import com.antheminc.oss.nimbus.entity.client.ClientEntity;
import com.antheminc.oss.nimbus.entity.client.access.ClientAccessEntity;
import com.antheminc.oss.nimbus.entity.client.access.ClientUserRole;
import com.antheminc.oss.nimbus.entity.user.AbstractUser;
import com.antheminc.oss.nimbus.entity.user.UserRole;
import com.antheminc.oss.nimbus.entity.user.UserStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Soham Chakravarti
 * @author Rakesh Patel
 * 
 */
@Domain(value="clientuser", includeListeners={ListenerType.persistence})
@Repo(alias="clientuser", value=Database.rep_mongodb)
@Getter @Setter @ToString(callSuper=true)
public class ClientUser extends AbstractUser<ClientUserRole> {
	
	private static final long serialVersionUID = 1L;
	
	@Ignore 
	private Client client;

	private List<UserRole> roles;
	private List<UserStatus> userStatuses;
	
	@ReadOnlyProperty
	private List<ClientAccessEntity> resolvedAccessEntities;
	private String roleName; // Temp for header
	
}
