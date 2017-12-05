package com.anthem.oss.nimbus.test.sample.domain.model.core;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Execution.Config;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;

/**
 * This class tests for accessConditional(s) for the user with roles {intake, clinician} and resolvedAuthorities = 'member_management'
 * @author Rakesh Patel
 *
 */
@Domain(value = "sample_core_nested", includeListeners = { ListenerType.persistence })
@Repo(Database.rep_mongodb)
@Getter
@Setter
public class SampleCoreEntityNestedConfig extends IdString {

	private static final long serialVersionUID = 1L;

	private String attr_String;
	
	private String testParam;
	
	private String testParam2;
	
	private String testParam3;
	
	@Config(url="/testParam3/_update?rawPayload=\"<!../<!../testParam2!>!>\"")
	private String paramConfigWithNestedPath;
	
	@Config(url="/testParam3/_update?rawPayload=\"<!../testParam!><!../testParam2!>\"")
	private String paramConfigWithNestedPath2;
}

