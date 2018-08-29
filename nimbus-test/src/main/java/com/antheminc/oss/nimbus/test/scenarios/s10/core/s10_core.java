package com.antheminc.oss.nimbus.test.scenarios.s10.core;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Domain(value="s10_core", includeListeners={ListenerType.persistence, ListenerType.update})
@Repo(value=Repo.Database.rep_mongodb, cache=Repo.Cache.rep_device)
@Getter @Setter @ToString(callSuper=true)
public class s10_core extends AbstractEntity.IdLong {

	List<OwnerCall> calls;
	
	static class OwnerCall{
		private String test;
	}
	
}


