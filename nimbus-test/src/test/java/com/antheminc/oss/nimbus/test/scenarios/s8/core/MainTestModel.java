package com.antheminc.oss.nimbus.test.scenarios.s8.core;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;
import com.antheminc.oss.nimbus.test.scenarios.s8.view.VChildTestModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Domain(value="maintestmodel", includeListeners={ListenerType.persistence, ListenerType.update}) 
@Repo(value=Database.rep_mongodb, cache=Cache.rep_device)
@Getter @Setter @ToString(callSuper=true)
public class MainTestModel extends IdLong {
	private static final long serialVersionUID = 1L;
	private String para1;
	private String para2;
	private List<ChildTestModel> child;

}
