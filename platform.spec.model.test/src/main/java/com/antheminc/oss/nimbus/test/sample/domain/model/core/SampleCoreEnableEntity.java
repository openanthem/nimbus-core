/**
 * 
 */
package com.antheminc.oss.nimbus.test.sample.domain.model.core;

import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.domain.definition.extension.EnableConditional;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Model
@Getter @Setter 
public class SampleCoreEnableEntity {
	
	@EnableConditional(when="state == 'Joker'", targetPath="../enable_p2")
	private String enable_p1;
	
	private String enable_p2;
}