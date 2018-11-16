package com.antheminc.oss.nimbus.test.exclude.core;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;

import lombok.Data;

@Domain(value = "sampleExcludeEntity", includeListeners = { ListenerType.websocket })
@Repo(value = Repo.Database.rep_none, cache = Repo.Cache.rep_device)
@Data
public class SampleExcludeEntity {

}
