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

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Type;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntityAccess;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value = "sample_core_access_view", includeListeners = { ListenerType.websocket })
@Type(SampleCoreEntityAccess.class)
//@ViewRoot(layout = "")
@Repo(value = Database.rep_none, cache = Cache.rep_device)
@Getter @Setter
public class VRSampleCoreEntityAccess {

	@Page(route="sample_core_access_view")
	private VPSampleCoreEntityAccess vpSampleCoreEntityAccess;
	
	@Model @Getter @Setter
	public static class VPSampleCoreEntityAccess {
		
		@Tile
		private VTSampleCoreEntityAccess vtSampleCoreEntityAccess;
		
	}
	
	@Model @Getter @Setter
	public static class VTSampleCoreEntityAccess {
		
		@Section
		private VSSampleCoreEntityAccess vsSampleCoreEntityAccess;
		
		@Section
		private VSSamplePageCoreEntityAccess vsSamplePageCoreEntityAccess;
	}
	
	@Model @Getter @Setter
	public static class VSSampleCoreEntityAccess {
		
		@MapsTo.Path(linked = false)
		@Config(url = "/vpSampleCoreEntityAccess/vtSampleCoreEntityAccess/vsSampleCoreEntityAccess/vgSampleCoreEntities.m/_process?fn=_set&url=/p/sample_core_access/_search?fn=example")
		@Grid(onLoad=true)
		private List<SampleCoreEntityAccessLineItem> vgSampleCoreEntities;
	}
	
	@Model @Getter @Setter
	public static class VSSamplePageCoreEntityAccess {
		// Discussion with Dinakar:
			// need paging vs no paging - no option to configure
			// where to define default pageSize - @Grid already has it.
		
		@MapsTo.Path(linked = false)
		@Config(url = "/vpSampleCoreEntityAccess/vtSampleCoreEntityAccess/vsSamplePageCoreEntityAccess/vgSamplePageCoreEntities.m/_process?fn=_set&url=/p/sample_core_access/_search?fn=query&<!page=y!>&where=<!filterCriteria!>")
		@Grid(onLoad=true)
		private List<SampleCoreEntityAccessLineItem> vgSamplePageCoreEntities;
	}
	
	

}