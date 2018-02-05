/**
 * 
 */
package com.antheminc.oss.nimbus.test.entity.sample.s1.view;

import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Type;
import com.antheminc.oss.nimbus.domain.defn.extension.AccessConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.AccessConditional.Permission;
import com.antheminc.oss.nimbus.domain.defn.extension.ActivateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.EnableConditional;
import com.antheminc.oss.nimbus.test.entity.sample.s1.core.S1C_AnotherMain;

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
