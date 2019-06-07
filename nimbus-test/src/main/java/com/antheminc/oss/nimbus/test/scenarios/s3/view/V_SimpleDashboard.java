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
package com.antheminc.oss.nimbus.test.scenarios.s3.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.test.scenarios.s3.core.ServiceLine;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="v_simpledashboard",includeListeners={ListenerType.websocket})
@Getter @Setter
public class V_SimpleDashboard {

	@MapsTo.Type(ServiceLine.class)
	@Getter @Setter
	public static class V_CardServiceLine {
		
		private String service;
	}
	
	@Model
	@Getter @Setter
	public static class Section_Main {
		
		@Path(linked=false)
		private List<V_CardServiceLine> serviceLines;
	} 
	
	@Model
	@Getter @Setter
	public static class Page_Landing {
		
		private Section_Main main;
	}
	
	@Page
	private Page_Landing pgLanding;
}
