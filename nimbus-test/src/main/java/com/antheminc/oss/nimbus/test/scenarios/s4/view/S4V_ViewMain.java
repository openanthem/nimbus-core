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
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Nature;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Type;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.extension.EnableConditional;
import com.antheminc.oss.nimbus.test.scenarios.s4.core.S4C_AnotherModel;
import com.antheminc.oss.nimbus.test.scenarios.s4.core.S4C_CoreMain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain("s4v_main") @Type(S4C_CoreMain.class)
@Repo(value=Database.rep_none, cache=Cache.rep_device)
@Getter @Setter @ToString
public class S4V_ViewMain {

	@Type(S4C_AnotherModel.class)
	@Getter @Setter @ToString
	public static class VLineItem {
		
		@Path
		private String str2;
		
		@Config(url="/form/_get?fn=param&expr=assignMapsTo('/.d/<!#this!>/../.m')")
		private String editLink;
	}
	
	
	@Type(S4C_AnotherModel.class)
	@Getter @Setter @ToString
	public static class VForm {

		@Path
		private String str1;
		
		@Path
		private String str2;
		
		
		private String unmappedAttr1;
		
		
		@Config(url="<!#this!>/../_replace")
		@Config(url="<!#this!>/../_get?fn=param&expr=flush()")
		private String saveButton;
		
		@Config(url="<!#this!>/../_get?fn=param&expr=unassignMapsTo()")
		private String cancelButton;
		
		@Config(url="<!#this!>/../.m/_delete")
		@Config(url="<!#this!>/../_get?fn=param&expr=unassignMapsTo()")	
		private String deleteButton;
	}
	
	@Config(url="<!#this!>/../form/_get?fn=param&expr=unassignMapsTo()")
	private String addButton;
	
	
	/**
	 * View grid to dynamically reflect updates to mapped core collection
	 */
	@Path("/anotherModeList")
	private List<VLineItem> summaryGrid;
	
	
	/**
	 * View form to collect data which may get added to collection mapped with "/anotherModeList"
	 */
	@Path(value="/anotherModeList", nature=Nature.TransientColElem)
	@EnableConditional(when="isAssigned()", targetPath="/deleteButton")
	@EnableConditional(when="!isAssigned()", targetPath="/cancelButton")
	private VForm form;
	
}
