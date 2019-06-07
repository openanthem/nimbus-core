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
/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.s1.view;

import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Type;
import com.antheminc.oss.nimbus.domain.defn.extension.AccessConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.AccessConditional.Permission;
import com.antheminc.oss.nimbus.domain.defn.extension.ActivateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.EnableConditional;
import com.antheminc.oss.nimbus.test.scenarios.s1.core.S1C_AnotherMain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Type(S1C_AnotherMain.class)
@Getter @Setter @ToString
public class S1V_LineItem {

	@EnableConditional(when="state == 'value1_0'", targetPath="/../vLink2")
	@Path
	private String value1;
	
	@ActivateConditional(when="state == 'value2_1'", targetPath="/../vOnly1")
	@Path
	private String value2;

	@AccessConditional(whenAuthorities="?[#this == 'theboss'].empty", p=Permission.READ)
	@Path
	private String value3;
	
	@AccessConditional(whenAuthorities="?[#this == 'theboss'].empty", p=Permission.HIDDEN)
	private String value4;
	
	private String vOnly1;
	
	private String vLink2;
}
