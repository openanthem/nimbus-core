package com.antheminc.oss.nimbus.test.scenarios.s0.core;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.GROUP_1;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.GROUP_2;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.GROUP_3;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationScope;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditionals;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Tony Lopez
 *
 */
@Model
@Getter @Setter 
public class SampleValidateEntity {

	public final static String G1_PATTERN_REGEX = "hooli-(.*)";
	
	@ValidateConditionals({
		@ValidateConditional(when = "state == 'rigby'", targetGroup = GROUP_1.class),
		@ValidateConditional(when = "state == 'hooli'", targetGroup = GROUP_2.class)
	})
	@TextBox(postEventOnChange = true)
	private String condition;
	
	@ValidateConditionals({
		@ValidateConditional(when = "state == 'rigby'", targetGroup = GROUP_1.class, scope = ValidationScope.SIBLING_NESTED),
		@ValidateConditional(when = "state == 'hooli'", targetGroup = GROUP_2.class, scope = ValidationScope.SIBLING_NESTED)
	})
	@TextBox(postEventOnChange = true)
	private String nested_condition;
	
	@NotNull
	@Pattern(regexp = G1_PATTERN_REGEX, groups = { GROUP_1.class })
	private String validate_p1;
	
	@NotNull(groups = { GROUP_1.class })
	@Pattern(regexp = G1_PATTERN_REGEX, groups = { GROUP_2.class })
	private String validate_p2;
	
	private Validate_P3 validate_p3;
	
	@Model @Getter @Setter
	public class Validate_P3 {
		
		@NotNull(groups = { GROUP_1.class, GROUP_2.class })
		private String validate_p3_1;
		
		private Validate_P3_2 validate_p3_2;
	}
	
	@Model @Getter @Setter
	public class Validate_P3_2 {
		
		@NotNull(groups = { GROUP_1.class, GROUP_2.class })
		private String validate_p3_2_1;
		
		@NotNull(groups = { GROUP_2.class })
		private String validate_p3_2_2;
	}
}
