
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
package com.antheminc.oss.nimbus.test.scenarios.s2.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Type;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridColumn;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridRowBody;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.test.scenarios.s2.core.S2C_LineItemB;
import com.antheminc.oss.nimbus.test.scenarios.s2.core.S2C_Row;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Domain("s2v_main")
@Repo(value=Repo.Database.rep_none, cache=Repo.Cache.rep_device)
@Getter @Setter
public class S2V_VRMain {
	/*
	 * 1. NestedCollection.elementConfig is different than what is sent in ParamConfig
	 * 		- DONE: rename attribs = type, model, params, param inside Config to end with 'config  
	 * 2. ElementConfig doesnt send nested model config
	 * 		- DONE
	 * 3. Setting of value to nested collection within row emits entire outer collection as update 
	 */
	
	@Grid
	@Config(url="/<!#this!>.m/_process?fn=_set&url=/p/s2c_row/_search?fn=query&where=something")
	@Path(linked=false)
	private List<V_Row> rows;

	
	@Type(S2C_Row.class)
	@Getter @Setter
	public static class V_Row {
		@GridColumn
		@Path
		private String topValue1;
		
		@GridColumn
		@Path
		private String topValue2;
		
		@GridRowBody
		private V_NestedRowBody nestedRowBody;
	}
	
	@Model
	@Getter @Setter
	public static class V_NestedRowBody {
		
		private String nestedRowBodyValue1;
		
		private String nestedRowBodyValue2;
		
		@Grid
		@Config(url="/<!#this!>.m/_process?fn=_set&url=/p/s2c_li_B/_search?fn=query&where=something")
		@Path(linked=false)
		private List<V_NestedRowBodyLineItem> nestedRowBodyLineItems;
		
		@Section
		private V_ExpandedRowView expandedRowView;
	}

	@Type(S2C_LineItemB.class)
	@Getter @Setter
	public static class V_NestedRowBodyLineItem {
		
		@Path
		private String li_v1;
		
		@Path
		private String li_v2;
	}
	
	@Getter @Setter
	public static class V_ExpandedRowView {
		@Path
		private String expandedRowViewValue1;
	}
	
	
}
