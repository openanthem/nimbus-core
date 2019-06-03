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
package com.antheminc.oss.nimbus.domain.cmd;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor
public enum Behavior {

	$execute("_execute"),	//default if not specified
	$state("_state"),
	$save("_save"),
	$nav("_nav"),
	$config("_config"),
	$validate("_validate");
	
	public static final Behavior DEFAULT = $execute;
	
	final private String code;
	
	
	//  .../p/flow_um-case/_process?b=$save
	
	//  .../p/flow_um-case/_findPatient/_process?b=$execute
	
	//  .../p/flow_um-case/_findPatient/_process?b=$executeAndSave	
	
	//	.../p/flow_um-case/requestType/_update?b=$executeAndSave

	// url-> .../icr/**/flow_e_ AND  	b=null 	==> b=$execute
	// url-> .../icr/p/flow_s_um-case	b==null	==> b=$execute&b=$save
	
	// url-> .../acmp/**/ AND  		b=null 	==> b=$executeAndSave  OR b=$execute&b=$save
	
	//		.../icr/p/flow_abc/patient/_save
}
