package com.antheminc.oss.nimbus.test.scenarios.s0.core;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity;
import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Trim;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Domain(value = "mypojo", includeListeners = { ListenerType.persistence })
@Repo(value = Database.rep_mongodb, cache = Cache.rep_device, alias="mypojo")
public class MyPojo extends AbstractEntity.IdLong{

	private static final long serialVersionUID = 724748124898081913L;
	
    @Trim
    @Parsed(field="Exception Category")
    private String myColumn1;

    @Trim
    @Parsed(field="Comments")
    private int myColumn2;
}
