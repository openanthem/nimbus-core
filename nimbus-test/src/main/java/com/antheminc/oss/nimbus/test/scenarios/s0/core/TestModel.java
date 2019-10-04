package com.antheminc.oss.nimbus.test.scenarios.s0.core;

import org.springframework.data.annotation.Transient;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Domain(value="testmodel_core", includeListeners={ListenerType.persistence, ListenerType.update})
@Repo(value=Repo.Database.rep_mongodb)
@Getter @Setter @ToString(callSuper=true)
public class TestModel extends AbstractEntity.IdLong {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String attr1;
	
	private String attr2;
	
	@Transient
	private String attr3;
	
	@Transient
	private String attr4;
	
	@Transient
	private InnerTestModel innerTestModel;
}
