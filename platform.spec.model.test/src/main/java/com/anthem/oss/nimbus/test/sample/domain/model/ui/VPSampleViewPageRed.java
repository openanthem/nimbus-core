/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
package com.anthem.oss.nimbus.test.sample.domain.model.ui;

import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.definition.Execution.Config;
import com.anthem.oss.nimbus.core.domain.definition.Executions.Configs;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Nature;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Tile;
import com.anthem.oss.nimbus.test.sample.domain.model.core.SampleCoreEntity;
import com.anthem.oss.nimbus.test.sample.domain.model.core.SampleCoreNestedEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@MapsTo.Type(SampleCoreEntity.class)
@Getter @Setter
public class VPSampleViewPageRed {

	@Tile(title="Sample Page Red", size=Tile.Size.Large) 
	private TileRed tile;	
	
	@MapsTo.Type(SampleCoreEntity.class)
	@Getter @Setter
	public static class TileRed {
	
		// D. add/edit transient collection element (via Form converted-B)
		@Path(value="/attr_list_2_NestedEntity", nature=Nature.TransientColElem)
		private Form_ConvertedNestedEntity vt_attached_convertedNestedEntity;
	}
	
	@MapsTo.Type(SampleCoreNestedEntity.class)
	@Getter @Setter
	public static class Form_ConvertedNestedEntity {
		
		// detect if in add mode vs. edit: add results in addition to collection elements whereas edit updates existing element
		@Configs(
			@Config(url="<!#this!>/../_update")	
		)
		private String saveButton;
		
		// 1. delete mapsTo elem	2. unassign
		@Configs({
			@Config(url="<!#this!>/../.m/_delete"),
			@Config(url="<!#this!>/../_get?fn=param&expr=unassignMapsTo()")	
		})
		private String deleteButton;
		
		@Path("/nested_attr_String")
		private String vt_nested_attr_String;
	}
	
}
