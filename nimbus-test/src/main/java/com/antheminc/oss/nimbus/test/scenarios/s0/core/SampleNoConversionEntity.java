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

import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.extension.ActivateConditional;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Model
@Getter @Setter
public class SampleNoConversionEntity {

	private NestedNoConversionLevel1 nc_nested_level1;
	
	@Model
	@Getter @Setter
	public static class NestedNoConversionLevel1 {
		
		@ActivateConditional(when="state == 'No'", targetPath="../nc_nested_level2")
		private String nested_nc_attr1A;
		
		//@ActivateConditional(when="state == '1B'", targetPath="../nc_nested_level2")
		private String nested_nc_attr1B;
		
		private NestedNoConversionLevel2 nc_nested_level2;
	}
	
	
	@Model
	@Getter @Setter
	public static class NestedNoConversionLevel2 {
		@ActivateConditional(when="state == 'other'", targetPath="../nc_nested_level3")
		private String nested_nc_attr2C;
				
		private NestedNoConversionLevel3 nc_nested_level3;
		
		private String nested_nc_attr2D;
	}
	
	@Model
	@Getter @Setter
	public static class NestedNoConversionLevel3 {
		private String nested_nc_attr3D;
	}
}
