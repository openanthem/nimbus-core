package com.antheminc.oss.nimbus.domain.model.state.repo.db;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.entity.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Domain(value="covid19", includeListeners={ListenerType.persistence, ListenerType.update})
@Repo(value=Repo.Database.rep_mongodb, cache=Repo.Cache.rep_device)
@Getter @Setter @ToString(callSuper=true) @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(callSuper = true)
public class CovidData extends AbstractEntity.IdLong{
	private static final long serialVersionUID = 1L;
	
	private String country;
	private int cases;
	private String continent;

}