package com.anthem.oss.nimbus.core.entity.queue;

import com.anthem.oss.nimbus.core.domain.definition.ConfigNature.Ignore;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Cache;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain(value="queue", includeListeners={ListenerType.persistence})
@Repo(value=Database.rep_mongodb, 
		namedNativeQueries = { @Repo.NamedNativeQuery(name = "userQueues", nativeQueries = {
			"{ \"aggregate\": \"queue\", \"pipeline\": [ { $graphLookup: { from: \"clientuser\", startWith: \"$entityId\", connectFromField: \"entityId\", connectToField: \"_id\", as: \"users\", restrictSearchWithMatch: { \"_id\": \"U2\" } } }, { $match: { \"users\": { $ne: [] } } } ] }",
			"{ \"aggregate\": \"queue\", \"pipeline\": [{ $graphLookup: { from: \"clientusergroup\", startWith: \"$entityId\", connectFromField: \"entityId\", connectToField: \"_id\", as: \"usergroups\", restrictSearchWithMatch: {\"members.userId\":\"U2\"} } }, { $match: { \"usergroups\" :{ $ne: []} } } ] }" }) },
		cache=Cache.rep_device)
@Getter @Setter @EqualsAndHashCode(of={"code"},callSuper=false)
public class Queue extends IdString {

	@Ignore
	private static final long serialVersionUID = 1L;
	
	private String displayName;
	
	private String name;
	
	private String description;
	
	private String status;
	
	private QueueType type;
	
	public enum QueueType {
		USER,
		USERGROUP
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
