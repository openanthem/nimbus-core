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
package com.antheminc.oss.nimbus.test.scenarios.s4.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Executions.Configs;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Button;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Button.Style;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Form;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Modal;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.test.scenarios.s4.core.MyData;
import com.antheminc.oss.nimbus.test.scenarios.s4.core.S4_MainCoreBackingObject;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Tony Lopez
 *
 */
@Domain(value="s4_MainCoreBackingObjectView", includeListeners={ListenerType.websocket})
@MapsTo.Type(S4_MainCoreBackingObject.class)
@Repo(Database.rep_none)
@Getter @Setter
public class S4_VRMainCoreBackingObjectView {

	@Page
	private VPMain vpMain;
	
	@Model @Getter @Setter
	public static class VPMain {
		
		@Tile
		private VTMain vtMain;
	}
	
	@Model @Getter @Setter
	public static class VTMain {
		
		@Modal
		private AddDataModal addDataModal;
		
		@Section
		private VSMain vsMain;
	}
	
	@Model @Getter @Setter
	public static class VSMain {
		
		@Form
		private VFMain vfMain;
	}
	
	@MapsTo.Type(S4_MainCoreBackingObject.class)
	@Getter @Setter
	public static class VFMain {
		
		@Button(style = Style.SECONDARY)
		@Label("Add Data")
		@Configs({
			@Config(url = "/vpMain/vtMain/addDataModal/vsAddDataModalBody/vfAddDataModalForm/_get?fn=param&expr=assignMapsTo('../../.m/myDataCollection')"),
//			@Config(url = "/vpMain/vtMain/addDataModal/_process?fn=_setByRule&rule=togglemodal")
		})
		private String addData;
		
		@Grid
		@Label("Allergies")
		private List<MyData> myDataCollection;
	}
}
