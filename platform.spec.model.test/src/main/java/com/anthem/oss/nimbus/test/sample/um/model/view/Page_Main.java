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
package com.anthem.oss.nimbus.test.sample.um.model.view;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Link;
import com.anthem.oss.nimbus.test.sample.um.model.core.ServiceLine;
import com.anthem.oss.nimbus.test.sample.um.model.core.UMCase;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@MapsTo.Type(UMCase.class)
@Getter @Setter
public class Page_Main {

	@MapsTo.Type(ServiceLine.class)
	@Getter @Setter
	public static class V_CardServiceLine {
		
		@Path("service") private String header;
	}
	
	@MapsTo.Type(ServiceLine.class)
	@Getter @Setter
	public static class V_CRUDServiceLine {
		// TODO F/w change to get collection.elemId
		//@TextBox(hidden=true)
		//==@Path private String elemId;
		
		@Path private String service;
		@Path private String something;
		
		@Link(url="/crudServiceLine/_process?b=$execute&stateUrl=/serviceLines/{elemId}") 
		private String crudServiceLine;
	}
	
	
	
	
	@Path("/serviceLines") 
	private List<V_CardServiceLine> vCardServiceLines;
	
	// MODE: null => unmapped(default), 
	// attached_final, attached_transient, detached  
	// attached=true(true)/false, temporal=true/false(default)
	
	@Path(linked=false)
	private V_CRUDServiceLine vCrudServiceLine;
}
