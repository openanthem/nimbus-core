/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command;

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
