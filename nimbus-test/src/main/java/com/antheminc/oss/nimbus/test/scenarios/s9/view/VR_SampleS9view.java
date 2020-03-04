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
package com.antheminc.oss.nimbus.test.scenarios.s9.view;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Form;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.test.scenarios.s9.core.SampeS9core;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sandeep Mantha
 *
 */
@Domain(value = "sample_s9_view", includeListeners = { ListenerType.websocket })
@MapsTo.Type(SampeS9core.class)
@Repo(value=Database.rep_none, cache=Cache.rep_device)
@Getter
@Setter
public class VR_SampleS9view {
	
	@Page
	private VPMain vp;
	
	@Model @Getter @Setter
	public static class VPMain {
	
		@Tile
		private VTMain vt;
	}
	
	@Model @Getter @Setter
	public static class VTMain {
		
		@Section
		private VSMain vs;
	}
	
	@Model @Getter @Setter
	public static class VSMain {
		
		@Form
		private VFMain vf;
	}
	
	@MapsTo.Type(SampeS9core.class)
	@Model @Getter @Setter
	public static class VFMain {
		
		@Path
		private String attr1;
		
		@Path
		private String attr2;
		
	}

}