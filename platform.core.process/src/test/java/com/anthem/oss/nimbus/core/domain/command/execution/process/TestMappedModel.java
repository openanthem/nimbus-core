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
package com.anthem.oss.nimbus.core.domain.command.execution.process;

import com.anthem.oss.nimbus.core.domain.definition.MapsTo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
//@Domain(value="testmappedmodel") 
@MapsTo.Type(TestModel.class) 
@Getter @Setter
public class TestMappedModel {
	
	private String parameter1;
	private String parameter2;
	
	@MapsTo.Path(linked=false)
	private String parameter3;
}
