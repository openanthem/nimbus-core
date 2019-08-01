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
/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridColumn;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridColumn.FilterMode;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Link;
import com.antheminc.oss.nimbus.domain.defn.extension.AccessConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.AccessConditional.Permission;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntityAccess;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@MapsTo.Type(SampleCoreEntityAccess.class)
@Getter @Setter
public class SampleCoreEntityAccessLineItem {

	@Path @GridColumn(filter = true, filterMode = FilterMode.contains)
	@AccessConditional(whenAuthorities="?[#this == 'case_management'].empty", p=Permission.READ)
	private String attr_String;
	
	@Path @GridColumn(filter = true, filterMode = FilterMode.equals)
	@AccessConditional(whenAuthorities="?[#this == 'case_management'].empty", p=Permission.READ)
	private String attr_String2;
	
	@Path("/accessConditional_Contains_Hidden1/nested_attr_String") @GridColumn(filter = true, filterMode = FilterMode.contains)
	@AccessConditional(whenAuthorities="?[#this == 'case_management'].empty", p=Permission.READ)
	private String nested_attr_String;
	
	@Path("/attr_LocalDate1") @GridColumn(filter = true, filterMode = FilterMode.contains, datePattern="MM/dd/yyyy")
	@AccessConditional(whenAuthorities="?[#this == 'case_management'].empty", p=Permission.READ)
	private LocalDate attr_LocalDate1;
	
	@Link(imgSrc = "edit.png")
	@AccessConditional(whenAuthorities="?[#this == 'case_management'].empty", p=Permission.HIDDEN)
	private String viewLink;
	
	@Path @GridColumn
	private ZonedDateTime attr_ZonedDateTime1;
}
