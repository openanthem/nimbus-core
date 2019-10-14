package com.antheminc.oss.nimbus.entity;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.client.Client;
import com.antheminc.oss.nimbus.entity.client.access.ClientAccessEntity;
import com.antheminc.oss.nimbus.entity.user.UserRole;
import com.antheminc.oss.nimbus.entity.user.UserStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Sandeep Mantha
 * 
 */
@Domain(value="lock", includeListeners={ListenerType.persistence})
@Repo(alias="lock", value=Database.rep_mongodb)
@Getter @Setter @ToString(callSuper=true)
public class LockEntity extends AbstractEntity.IdLong {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String domain;
	private String lockedBy;
	
}
