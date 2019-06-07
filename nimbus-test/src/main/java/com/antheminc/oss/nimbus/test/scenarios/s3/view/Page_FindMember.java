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

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Button;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Calendar;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ComboBox;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Hints;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Hints.AlignOptions;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.test.scenarios.s3.core.Member;
import com.antheminc.oss.nimbus.test.scenarios.s3.core.SimpleCase;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@MapsTo.Type(SimpleCase.class)
@Getter @Setter
public class Page_FindMember {
	
	@MapsTo.Type(SimpleCase.class)
	@Getter @Setter
	public static class Section_CaseInfo  {

		@Path @ComboBox private String requestType;

		@Path @ComboBox private String caseType;
		//@MapsTo.Path private DateRange serviceDate;
    }
	
	

	@MapsTo.Type(value=Member.class)
	@Getter @Setter
	public static class Form_MemberInfo {

		@Path @NotNull @TextBox private String subscriberId;

		@Path @TextBox private String firstName;

		@Path @TextBox private String lastName;

		@Path @Calendar private LocalDate dob;

		@Button(url="{:id}/_findPatient/_process") @Hints(value=AlignOptions.Left)
		private String action_FindMember;

		@Button(url="/_nav?a=next") @Hints(value=AlignOptions.Right)
		private String next;
	}
    

	
	@Getter @Setter
	public static class Section_MemberInfo {

		//@Form 
		//@Path(linked=false) private Form_PatientInfo memberInfoEntry;
	}

	
	
	@Section
	private Section_CaseInfo caseInfo;

	@Section
	private Section_MemberInfo memberInfo;

}
