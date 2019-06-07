/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.test.scenarios.s0.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.extension.AccessConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.AccessConditional.Permission;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

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
public class SampleCoreEntityAccess extends IdLong {

	private static final long serialVersionUID = 1L;

	private String attr_String;
	
	private String attr_String2;
	
	private LocalDate attr_LocalDate1;
	
	private LocalDateTime attr_LocalDateTime1;
	
	private ZonedDateTime attr_ZonedDateTime1;
	
	private Date attr_Date1;

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
	
	@AccessConditional(whenAuthorities="!?[#this == 'case_management'].empty && ?[#this == 'task_management'].empty", p=Permission.READ)
	@AccessConditional(whenAuthorities="?[#this == 'case_management'].empty && ?[#this == 'task_management'].empty", p=Permission.HIDDEN)
	private SampleCoreNestedEntity accessConditional_WhenAuthorities_Read2;
	
	@AccessConditional(whenAuthorities="!?[#this == 'case_management'].empty && !?[#this == 'member_management'].empty", p=Permission.HIDDEN)
	private SampleCoreNestedEntity accessConditional_WhenAuthorities_Hidden2;

}