package com.anthem.oss.nimbus.test.sample.domain.model.core;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.domain.definition.extension.AccessConditional;
import com.anthem.oss.nimbus.core.domain.definition.extension.AccessConditional.Permission;
import com.anthem.oss.nimbus.core.domain.definition.extension.AccessConditional.R2P;
import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value = "sample_core_access", includeListeners = { ListenerType.persistence })
@Repo(Database.rep_mongodb)
@Getter
@Setter
public class SampleCoreEntityAccess extends IdString {

	private static final long serialVersionUID = 1L;

	private String attr_String;

	@AccessConditional({@R2P(r="hero", p={Permission.READ}), 
						@R2P(r="superhero", p={Permission.WRITE})})
	private SampleCoreNestedEntity q1Level1;

}