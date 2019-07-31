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
package com.antheminc.oss.nimbus.test.scenarios.s2.core;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Soham Chakravarti
 *
 */
@Domain("s2c_row")
@Getter @Setter @ToString
public class S2C_Row {

	@Model
	@Getter @Setter
	public static class Nested2LevelElem {
		
		private String nested2Value1;
	}
	
	@Model
	@Getter @Setter
	public static class NestedInRow {
		private String nestedValue1;
		private String nestedValue2;
		
		private List<Nested2LevelElem> nestedRowCollection;
	}
	
	private String topValue1;
	private String topValue2;
	
	private NestedInRow nestedInRow;
}
