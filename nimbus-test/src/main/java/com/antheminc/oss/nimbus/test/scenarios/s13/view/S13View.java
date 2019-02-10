/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.antheminc.oss.nimbus.test.scenarios.s13.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.domain.defn.extension.ActivateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.test.scenarios.s13.core.S13Core;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tony Lopez
 *
 */
@Domain(value="s13_view", includeListeners = { ListenerType.websocket })
@MapsTo.Type(S13Core.class)
@Repo(Database.rep_none)
@Getter @Setter
public class S13View {

	@Page
	private VPMain page;
	
	@Model
	@Getter @Setter
	public static class VPMain {
	
		@Tile 
		private VTTile tile;	
	}
	
	@MapsTo.Type(S13Core.class)
	@Getter @Setter
	public static class VTTile {

		@Section
		private SampleFilterSection filterSection;
		
		@Section
		private SampleSection1 section1;
		
		@Section
		private SampleSection2 section2;
    }
	
	@Model
	@Getter @Setter
	public static class SampleFilterSection {
		
		@ActivateConditional(when = "state == '1'", targetPath = "/../../section1")
		@ActivateConditional(when = "state == '2'", targetPath = "/../../section2")
		private String filter;
	}
	
	@Model
	@Getter @Setter
	public static class SampleSection1 {
		
		@MapsTo.Path(linked = false)
		@Config(url = "<!#this!>.m/_process?fn=_set&url=/p/s13_core/_search?fn=query&where=s13_core.attr_int.eq(42)")
		private List<S13SampleLineItem> grid;
	}
	
	@Model
	@Getter @Setter
	public static class SampleSection2 {
		
		@MapsTo.Path(linked = false)
		private List<S13SampleLineItem> grid;
	}
}
