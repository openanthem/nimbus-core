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
package com.anthem.oss.nimbus.test.sample.um.model.view;


import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Button;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Hints;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Hints.AlignOptions;
import com.anthem.oss.nimbus.test.sample.um.model.core.Patient;
import com.anthem.oss.nimbus.test.sample.um.model.core.UMCase;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@MapsTo.Type(UMCase.class)
@Getter @Setter
public class Page_PatientDetails {

	
	
	@MapsTo.Type(Patient.class)
	@Getter @Setter
	public static class Section_PatientDetailsDisplay {

		@Path private String subscriberId;

		@Path private String firstName;

		@Path private String lastName;
		
		//@MapsTo.Path("../provider/id")
		//private Object providerId;
    }

	//@Summary @Mode(Mode.Options.ReadOnly) @MapsTo.Path("/patient")
	//private Section_PatientDetailsDisplay existingPatient;
	
	/*	*/

	
	
	//@Summary @Mode(Mode.Options.ReadOnly) //@MapsTo.Path("/patient")
	//@Path(linked=false) private transient Section_PatientDetailsDisplay searchedPatient;

	@Button(url="/_nav?a=back") @Hints(value=AlignOptions.Left)
	private String back_FindPatient;

	@Button(url="/_nav?a=next") @Hints(value=AlignOptions.Right)
	private String action_ConfirmPatient;
	
}
