/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.domain.model.core;

import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.extension.ActivateConditional;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Model
@Getter @Setter
public class SampleNoConversionEntity {

	private NestedNoConversionLevel1 nc_nested_level1;
	
	@Model
	@Getter @Setter
	public static class NestedNoConversionLevel1 {
		
		@ActivateConditional(when="state == 'No'", targetPath="../nc_nested_level2")
		private String nested_nc_attr1A;
		
		//@ActivateConditional(when="state == '1B'", targetPath="../nc_nested_level2")
		private String nested_nc_attr1B;
		
		private NestedNoConversionLevel2 nc_nested_level2;
	}
	
	
	@Model
	@Getter @Setter
	public static class NestedNoConversionLevel2 {
		@ActivateConditional(when="state == 'other'", targetPath="../nc_nested_level3")
		private String nested_nc_attr2C;
				
		private NestedNoConversionLevel3 nc_nested_level3;
	}
	
	@Model
	@Getter @Setter
	public static class NestedNoConversionLevel3 {
		private String nested_nc_attr3D;
	}
}
