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


import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Button;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Hints;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Hints.AlignOptions;
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
public class Page_MemberDetails {

	@MapsTo.Type(Member.class)
	@Getter @Setter
	public static class Section_PatientDetailsDisplay {

		@Path private String subscriberId;

		@Path private String firstName;

		@Path private String lastName;
    }

	@Button(url="/_nav?a=back") @Hints(value=AlignOptions.Left)
	private String back_FindMember;

	@Button(url="/_nav?a=next") @Hints(value=AlignOptions.Right)
	private String action_ConfirmMember;
	
}
