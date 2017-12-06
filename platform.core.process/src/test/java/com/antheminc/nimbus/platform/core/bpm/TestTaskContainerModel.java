package com.anthem.nimbus.platform.core.bpm;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Cache;
import com.antheminc.oss.nimbus.core.domain.definition.Repo.Database;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain(value="testtaskcontainermodel", includeListeners={ListenerType.persistence, ListenerType.update}, lifecycle="testtaskcontainermodel") 
@Repo(value=Database.rep_mongodb, cache=Cache.rep_device)
@Getter @Setter @ToString(callSuper=true)
public class TestTaskContainerModel extends IdString {
	private static final long serialVersionUID = 1L;
	private String triggerParameter;
	private String taskProgressed;
	
}
