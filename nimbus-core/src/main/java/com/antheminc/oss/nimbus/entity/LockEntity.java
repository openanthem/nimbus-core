package com.antheminc.oss.nimbus.entity;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Sandeep Mantha
 * 
 *  * Scenario 1 - 
 * user1 - session 1 - obtains lock on domain 1
 * user2 - session 2 - when tries to obtain lock of domain 1 gets entity locked exception
 * 
 * Scenario 2 - 
 * user1 - session 1 - obtains lock on domain 1
 * user1 - session 1 - lands on root domain 
 * -- remove locks of domain1 and remove domain1 from cache
 *					
 * Scenario 3 - 
 * user 1 - session 1 - obtains lock on domain 1
 * user 1 - session 1 - obtains lock on domain 2
 * user 1 - session 2 - navigates to domain 1 --na
 * user 1 - session 2 - lands on root domain --na
 * -- remove lock on domain 1 and domain 2 (query based on session) and remove domain 1 and domain 2 from cache
 * 
 * diagram to illustrate unlocking
 * 
 * 
 */
@Domain(value="lock", includeListeners={ListenerType.persistence})
@Repo(alias="lock", value=Database.rep_mongodb)
@Getter @Setter @ToString(callSuper=true)
public class LockEntity extends AbstractEntity.IdLong implements DomainEntityLock<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String domain;
	private String lockedBy;
	private String sessionId;
	
}
