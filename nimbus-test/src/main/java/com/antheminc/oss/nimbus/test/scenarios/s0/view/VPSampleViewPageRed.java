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
package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Executions.Configs;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Nature;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Modal;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity.Level1;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@MapsTo.Type(SampleCoreEntity.class)
@Getter @Setter
public class VPSampleViewPageRed {

	@Tile(size=Tile.Size.Large) 
	private TileRed tile;
	
	@MapsTo.Type(SampleCoreEntity.class)
	@Getter @Setter
	public static class TileRed {
	
		// D. add/edit transient collection element (via Form converted-B)
		@Path(value="/attr_list_2_NestedEntity", nature=Nature.TransientColElem)
		private Form_ConvertedNestedEntity vt_attached_convertedNestedEntity;
		
		@Modal
		private VMRedModal modal;
	}

	@Model @Getter @Setter
	public static class VMRedModal {
		
		@Section
		private VSRedSection section;
	}
	
	@MapsTo.Type(SampleCoreEntity.class)
	@Model @Getter @Setter
	public static class VSRedSection {
		
		@Path(value="/attr_list_3_NestedEntity", nature=Nature.TransientColElem)
		private Form_ConvertedNestedEntity vf_attached_convertedNestedEntity;
	}
	
	@MapsTo.Type(SampleCoreNestedEntity.class)
	@Getter @Setter
	public static class Form_ConvertedNestedEntity {
		
		// detect if in add mode vs. edit: add results in addition to collection elements whereas edit updates existing element
		@Config(url="<!#this!>/../_replace")	
		@Config(url="<!#this!>/../_get?fn=param&expr=flush()")
		private String saveButton;
		
		// 1. delete mapsTo elem	2. unassign
		@Configs({
			@Config(url="<!#this!>/../.m/_delete"),
			@Config(url="<!#this!>/../_get?fn=param&expr=unassignMapsTo()")	
		})
		private String deleteButton;
		
		@NotNull
		@Path("/nested_attr_String")
		private String vt_nested_attr_String;
		
		@Path("/nested_attr_String2")
		private String vt_nested_attr_String2;
		
		@Path("/nested_attr_String3")
		private String vt_nested_attr_String3_1;
		
		@Path("/nested_attr_String3")
		private String vt_nested_attr_String3_2;
		
		@Path("/nested_attr_collection")
		private List<String> vt_nested_attr_collection; 
		
		@Path("/nested_attr_complex_collection")
		private List<Level1> vt_nested_attr_complex_collection; 
	}
	
}
