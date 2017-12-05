/**
 * 
 */
package com.antheminc.oss.nimbus.test.sample.domain.model.core;


import java.util.List;

import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.domain.definition.extension.ActivateConditional;
import com.antheminc.oss.nimbus.core.domain.definition.extension.Audit;

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
	
	private List<String> attr_list_String_noConversion;
	
	@ActivateConditional(when="state == 'Y'", targetPath="../level2")
	private String level1Attrib;
	
	private Level2 level2;

	private Level2b level2b;
	
	@Model @Getter @Setter
	public static class Level2 {
	
		private String level2Attrib;
		
		private String[] string_array;
	}
	
	@Model @Getter @Setter
	public static class Level2b {
	
		private String level2Attrib_b;
		
		private String[] string_array_b;
	}
}
