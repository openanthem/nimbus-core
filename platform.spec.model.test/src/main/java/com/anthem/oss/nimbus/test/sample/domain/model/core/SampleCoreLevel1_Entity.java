/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.domain.model.core;


import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.extension.ActivateConditional;
import com.anthem.oss.nimbus.core.domain.definition.extension.Audit;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Model
@Getter @Setter
public class SampleCoreLevel1_Entity {
	
	@Audit(SampleCoreAuditEntry.class)
	private String audit_nested_attr;
	
	@ActivateConditional(when="state == 'Y'", targetPath="../level")
	private String level1Attrib;
	
	private Level2 level;

	@Model @Getter @Setter
	public static class Level2 {
	
		private String level2Attib;
	}
	
}
