/**
 * 
 */
package com.antheminc.oss.nimbus.test.entity.sample.s1.core;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.extension.ActivateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.EnableConditional;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdString;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain("s1c_another")
@Getter @Setter @ToString(callSuper=true)
@SuppressWarnings("serial")
public class S1C_AnotherMain extends IdString {

	@EnableConditional(when="state == 'value1_0'", targetPath="/../cLink2")
	private String value1;
	
	@ActivateConditional(when="state == 'value2_1'", targetPath="/../cOnly1")
	private String value2;
	
	private String cOnly1;
	
	private String cLink2;
}
