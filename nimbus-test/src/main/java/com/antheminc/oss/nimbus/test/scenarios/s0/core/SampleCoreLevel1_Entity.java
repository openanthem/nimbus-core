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
package com.antheminc.oss.nimbus.test.scenarios.s0.core;


import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.ConfigNature.Ignore;
import com.antheminc.oss.nimbus.domain.defn.extension.ActivateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.Audit;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Model
@Getter @Setter
public class SampleCoreLevel1_Entity {
	
	@Audit(SampleCoreAuditEntry.class)
	private String audit_nested_attr;
	
	private List<String> attr_list_String_noConversion;
	
	@ActivateConditional(when="state == 'Y'", targetPath="../level2")
	private String level1Attrib;
	
	private Level2 level2;

	private Level2b level2b;
	
	@Ignore
	private String ignoredField;
	
	@Model @Getter @Setter
	public static class Level2 {
	
		private String level2Attrib;
		
		private String[] string_array;
	}
	
	@Model @Getter @Setter
	public static class Level2b {
	
		private String level2Attrib_b;
		
		private String[] string_array_b;
	}
}
