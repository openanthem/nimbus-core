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

import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Button;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Hints;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Hints.AlignOptions;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.test.scenarios.s3.core.ServiceLine;
import com.antheminc.oss.nimbus.test.scenarios.s3.core.SimpleCase;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@MapsTo.Type(SimpleCase.class)
@Getter @Setter
public class Page_Pg3 {

	private String mappedCaseId;
	
	@MapsTo.Type(ServiceLine.class)
	@Getter @Setter
	public static class Section_ServiceLine {
		
		private String someViewOnlyParam;
		
		@Path private String service;
	}
	
	@Path("/caseType") private @TextBox String aloha;

	@Button(url="/_nav?a=back") @Hints(value=AlignOptions.Left)
	private String back;
	
	
	@Path("/oneServiceLine") private ServiceLine coreAttachedOneServiceLine;
	@Path(linked=false) private ServiceLine noConversionDetachedOneServiceLine;
	
	@Path("/oneServiceLineConverted") private Section_ServiceLine viewAttachedOneServiceLineConverted;
	@Path(linked=false) private Section_ServiceLine convertedDetachedOneServiceLine;
	
	
	// collection mapping 1: primitive list direct mapping
	@Path private String[] types;

	// collection mapping 2: List of CoreModel mapping directly to List of CoreModel
	@Path("/serviceLines") private List<ServiceLine> noConversionAttachedColServiceLines;
	
	@Path(linked=false) private List<ServiceLine> noConversionDetachedColServiceLines;

	
	// collection mapping 4: ATTACHED: List of ViewModel mapping to List of CoreModel: ViewModel is mapped to CoreModel
	@Path("/serviceLinesConverted") private List<Section_ServiceLine> viewAttachedServiceLinesConverted;
	
	// collection mapping 5: DEATACHED: List of ViewModel mapping to List of CoreModel: ViewModel is mapped to CoreModel
	@Path(linked=false) private List<Section_ServiceLine> viewDetachedServiceLinesConverted;

	// collection mapping 6: ATTACHED: List of primitive mapping to one param inside List elem of CoreModel
	@Path(value="/serviceLines", colElemPath="/service") List<String> attachedNestedColAttribServices;
	
	// collection mapping 7: DETACHED: List of primitive mapping to one param inside List elem of CoreModel
	//@Path(value="/serviceLines/{index}/service", linked=false) List<String> detachedNestedAttribServices;
}
