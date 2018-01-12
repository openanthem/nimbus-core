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
package com.anthem.oss.nimbus.core.entity.queue;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Cache;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain(value="queue", includeListeners={ListenerType.persistence})
@Repo(value=Database.rep_mongodb, 
		namedNativeQueries = { @Repo.NamedNativeQuery(name = "userQueues", nativeQueries = {
			"{ \"aggregate\": \"queue\", \"pipeline\": [ { $match: { \"queueType\": \"USER\" } }, { $graphLookup: { from: \"clientuser\", startWith: \"$entityId\", connectFromField: \"entityId\", connectToField: \"_id\", as: \"users\", restrictSearchWithMatch: { \"_id\": \"<!#self/id!>\" } } }, { $match: { \"users\": { $ne: [] } } } ] }",
			"{ \"aggregate\": \"queue\", \"pipeline\": [ { $match: { \"queueType\": \"USERGROUP\" } }, { $graphLookup: { from: \"clientusergroup\", startWith: \"$entityId\", connectFromField: \"entityId\", connectToField: \"_id\", as: \"usergroups\", restrictSearchWithMatch: {\"members.userId\":\"<!#self/id!>\"} } }, { $match: { \"usergroups\" :{ $ne: []} } } ] }" }) },
		cache=Cache.rep_device)
@Getter @Setter
public class Queue extends IdString {

	private static final long serialVersionUID = 1L;
	
	private String displayName;
	
	private String name;
	
	private String description;
	
	private String status;
	
	private QueueType queueType;
	
	public enum QueueType {
		USER,
		USERGROUP;
	}
	
	// Queue to User & UserGroup is 1-1 relation based on NIM-3656,3657
	//@AssociatedEntity(clazz=MUser.class)
	//private Set<String> users;
	
	//private Set<String> userGroups;
	
	private String entityId;
//	
//	public void addUsers(MUser usr) {
//		if(getUsers() == null) {
//			setUsers(new HashSet<>());
//		}
//		getUsers().add(usr.getName());
//	}
//	
//	public void addUserGroups(MUserGroup userGroup) {
//		if(getUserGroups() == null) {
//			setUserGroups(new HashSet<>());
//		}
//		getUserGroups().add(userGroup.getName());
//	}
}
