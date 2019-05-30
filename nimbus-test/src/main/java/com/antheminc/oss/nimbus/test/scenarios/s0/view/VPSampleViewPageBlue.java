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

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Executions.Configs;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@MapsTo.Type(SampleCoreEntity.class)
@Getter @Setter
public class VPSampleViewPageBlue {


	@Tile(size=Tile.Size.Large) 
	private TileBlue tile;	
	
	@MapsTo.Type(SampleCoreEntity.class)
	@Getter @Setter
	public static class TileBlue {
		
		// A. add new Nested Entity (via Form converted-A)
		@Configs(
			@Config(url="/page_red/tile/vt_attached_convertedNestedEntity/_get?fn=param&expr=unassignMapsTo()")	
		)
		private String addButton;

		// B. view/edit mapped collection to core (via converted-B)
		@Path(value="/attr_list_2_NestedEntity")
		private List<Section_ConvertedNestedEntity> vm_attached_convertedList;
		
		// B. show drop down code-value to core
		//==TODO

    }
	
	@MapsTo.Type(SampleCoreNestedEntity.class)
	@Getter @Setter
	public static class Section_ConvertedNestedEntity {
		
		// view/edit link to Form converted-A: by assigning collection element of core to Form
		@Configs(
			@Config(url="/page_red/tile/vt_attached_convertedNestedEntity/_get?fn=param&expr=assignMapsTo('/.d/<!#this!>/../.m')")
		)
		private String editButton;
		
		@Path("/nested_attr_String")
		private String vm_nested_attr_String;
	}

}
