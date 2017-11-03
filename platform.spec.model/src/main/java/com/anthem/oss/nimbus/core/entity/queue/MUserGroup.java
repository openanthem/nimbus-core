package com.anthem.oss.nimbus.core.entity.queue;

import java.util.HashSet;
import java.util.Set;

import com.anthem.oss.nimbus.core.domain.definition.AssociatedEntity;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Cache;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;

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
