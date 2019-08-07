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

import com.antheminc.oss.nimbus.domain.defn.Converters;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.domain.model.state.internal.IdParamConverter;
import com.antheminc.oss.nimbus.test.scenarios.s3.core.SimpleCase;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="view_simplecase",includeListeners={ListenerType.websocket}) 
@MapsTo.Type(SimpleCase.class)
@Getter @Setter
public class VRSimpleCaseFlow {

//	@Page @Mode(Options.ReadOnly) //@ViewType(Page/Section/Form/Button/TextBox/Radio/Select/)
//	private Page_FindPatient pg1;
//
//	@Page
//	private Page_PatientDetails pg2;

	private String umcaseId;
	
	@MapsTo.Path("id")
	@Converters(converters={IdParamConverter.class})
	private String displayId;
	
	//@Values(url="staticCodeValue-/orgStatus")
	private String selectedValue_1;
	
	//@Values(url="staticCodeValue-/orgStatus")
	private String selectedValue_2;
	
	
	@Page
	private Page_Pg3 pg3;
	
//	// 1: mapped to refId param within view, will look for value within viewModel.refId, else will instantiate new core model
//	@Page @Path(linked=false, value="/umcase:{refId}/c/_get", kv=@KeyValue(k="refId", v="/umcaseId"))
//	private Page_Pg3 pg3;
//
//	// 2: mapped to refId param within nested view model which is self mapped; will look for value within viewModel.refId, else will instantiate new core model
//	@Page @Path(linked=false, value="/umcase:{refId}/c/_get", kv=@KeyValue(k="refId", v="/pg3b/mappedCaseId"))
//	private Page_Pg3 pg3b;
//
//	// 3. gets value by making an executor call; will use refId value if found, otherwise will instantiate new core model
//	@Page @Path(linked=false, value="/umcase:{refId}/c/_get", kv=@KeyValue(k="refId", v="/umcase:100/_get/id", quad=false))
//	private Page_Pg3 pg3c;
//
//	// 4. no mapping given, will instantiate new core model initially
//	@Page @Path(linked=false)
//	private Page_Pg3 pg3d;
	
	@Page
	private Page_Main main;
}
