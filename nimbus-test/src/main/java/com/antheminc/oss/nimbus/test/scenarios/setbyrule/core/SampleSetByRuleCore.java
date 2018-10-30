package com.antheminc.oss.nimbus.test.scenarios.setbyrule.core;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Domain(value="sample_setbyrule_core", includeListeners={ListenerType.persistence})
@Repo(Database.rep_mongodb)
@Getter @Setter @ToString(callSuper=true)
public class SampleSetByRuleCore extends IdLong{

	private static final long serialVersionUID = 1L;
	
	private Long associatedParamId;
	
	private String attr1;
	
	@Config(url="/_process?fn=_setByRule&rule=setByRuleDecisionTable&associatedParam=/p/decisiontabletestcoremodel:<!/../associatedParamId!>/_get")
	private String attr2;
	
	private String attr3;
}
