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
package com.antheminc.oss.nimbus.entity.queue;

import java.util.HashSet;
import java.util.Set;

import com.antheminc.oss.nimbus.domain.defn.AssociatedEntity;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value="usergroup", includeListeners={ListenerType.persistence})
@Repo(value=Database.rep_mongodb, cache=Cache.rep_device)
@Getter @Setter
public class MUserGroup extends AbstractEntity.IdString {

	private static final long serialVersionUID = 1L;

	private String name;
	
	@AssociatedEntity(clazz=Queue.class)
	private Set<String> queues;
	
	private Set<String> nestedGroups;
	
	private Set<String> groupMembers;
	
	public void addQueues(Queue q) {
		if(getQueues() == null) {
			setQueues(new HashSet<>());
		}
		getQueues().add(q.getName());
	}
	
	public void addNestedGroups(MUserGroup ug) {
		if(getNestedGroups() == null) {
			setNestedGroups(new HashSet<>());
		}
		getNestedGroups().add(ug.getName());
	}
	
	public void addGroupMembers(MGroupMember groupMember) {
		if(getGroupMembers() == null) {
			setGroupMembers(new HashSet<>());
		}
		getGroupMembers().add(groupMember.getName());
	}
}
