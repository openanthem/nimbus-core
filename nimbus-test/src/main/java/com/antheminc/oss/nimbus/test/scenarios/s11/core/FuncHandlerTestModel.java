package com.antheminc.oss.nimbus.test.scenarios.s11.core;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.extension.ConfigConditional;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;

@Domain(value="sample_functest", includeListeners={ListenerType.persistence})
@Repo(Database.rep_mongodb)
@Getter @Setter
public class FuncHandlerTestModel extends IdLong{
	private static final long serialVersionUID = 1L;
	@ConfigConditional(config= {
			@Config(url="/parameter2/_replace?rawPayload=\"Value\"")
	})
	private String parameter1;
	private String parameter2;
	
}
