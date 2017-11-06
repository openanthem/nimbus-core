package com.anthem.oss.nimbus.test.sample.domain.model.core;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.domain.definition.extension.AccessConditional;
import com.anthem.oss.nimbus.core.domain.definition.extension.AccessConditional.Permission;
import com.anthem.oss.nimbus.core.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;

/**
 * This class tests for accessConditional(s) for the user with roles {intake, clinician} and resolvedAuthorities = 'member_management'
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

	@AccessConditional(containsRoles="intake", p=Permission.HIDDEN)
	private SampleCoreNestedEntity accessConditional_Contains_Hidden1;
	
	@AccessConditional(containsRoles={"intake","clinician"}, p=Permission.HIDDEN)
	private SampleCoreNestedEntity accessConditional_Contains_Hidden2;
	
	@AccessConditional(containsRoles={"intake"}, p=Permission.READ)
	private SampleCoreNestedEntity accessConditional_Contains_Read;
	
	@AccessConditional(whenRoles="!?[#this == 'intake'].empty", p=Permission.READ)
	private SampleCoreNestedEntity accessConditional_WhenRoles_Read1;
	
	@AccessConditional(whenRoles="!?[#this == 'intake'].empty && !?[#this == 'casemanager'].empty", p=Permission.READ)
	private SampleCoreNestedEntity accessConditional_WhenRoles_Read2;
	
	@AccessConditional(whenRoles="!?[#this == 'intake'].empty && !?[#this == 'clinician'].empty", p=Permission.HIDDEN)
	private SampleCoreNestedEntity accessConditional_WhenRoles_Hidden1;
	
	@AccessConditional(whenRoles="!?[#this == 'intake'].empty || !?[#this == 'casemanager'].empty", p=Permission.HIDDEN)
	private SampleCoreNestedEntity accessConditional_WhenRoles_Hidden2;
	
	@AccessConditional(whenAuthorities="?[#this == 'case_management'].empty", p=Permission.HIDDEN)
	private SampleCoreNestedEntity accessConditional_WhenAuthorities_Hidden1;
	
	@AccessConditional(whenAuthorities="!?[#this == 'case_management'].empty", p=Permission.READ)
	private SampleCoreNestedEntity accessConditional_WhenAuthorities_Read2;
	
	@AccessConditional(whenAuthorities="!?[#this == 'case_management'].empty && !?[#this == 'member_management'].empty", p=Permission.HIDDEN)
	private SampleCoreNestedEntity accessConditional_WhenAuthorities_Hidden2;

}