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

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Model
@Getter @Setter
public class SampleCoreNestedEntity {
	
	private String nested_attr_String;
	
	private String nested_attr_String2;
	
	private String nested_attr_String3;
	
	private List<String> nested_attr_collection;
	
	private List<Level1> nested_attr_complex_collection;
	
	@Model
	@Getter @Setter
	public static final class Level1 {
		
		private String string1;
	}
}
