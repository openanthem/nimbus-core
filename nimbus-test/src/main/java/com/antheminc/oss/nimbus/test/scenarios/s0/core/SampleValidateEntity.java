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
package com.antheminc.oss.nimbus.test.scenarios.s0.core;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.GROUP_1;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.GROUP_2;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.GROUP_3;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.GROUP_4;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.GROUP_5;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.GROUP_6;
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
		@ValidateConditional(when = "state == 'hooli'", targetGroup = GROUP_2.class),
		@ValidateConditional(when = "state != 'rigby'", targetGroup = GROUP_3.class)
	})
	@TextBox(postEventOnChange = true)
	private String condition;
	
	@ValidateConditionals({
		@ValidateConditional(when = "state == 'rigby'", targetGroup = GROUP_1.class),
		@ValidateConditional(when = "state == 'hooli'", targetGroup = GROUP_2.class)
	})
	@TextBox(postEventOnChange = true)
	private String nested_condition;
	
	@ValidateConditionals({
		@ValidateConditional(when = "state != 'other'", targetGroup = GROUP_4.class),
		@ValidateConditional(when = "state == 'paloalto'", targetGroup = GROUP_5.class)
	})
	@TextBox(postEventOnChange = true)
	private String condition_3;
	
	@ValidateConditional(when = "state == null", targetGroup = GROUP_6.class)
	private String condition_4;
	
	@ValidateConditional(when = "state == 'hello'", targetPath = "../../attr_validate_nested_2/q1/q1_1", targetGroup = GROUP_1.class)
	private String condition_5;
	
	@ValidateConditional(when = "state == 'hello'", targetPath = "../../attr_validate_nested_2", targetGroup = GROUP_1.class, scope = ValidationScope.CHILDREN)
	private String condition_6;
	
	@ValidateConditional(when = "state == 'hello'", targetPath = { "../../attr_validate_nested_2/q1/q1_1", "../../attr_validate_nested_2/q1/nested/q1_2_1"} , targetGroup = GROUP_1.class)
	private String condition_7;
	
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
	
	@NotNull(groups = { GROUP_3.class })
	private String validate_p4;
	
	@NotNull(groups = { GROUP_4.class })
	private String validate_p5;
	
	@NotNull(groups = { GROUP_5.class })
	private String validate_p6;
	
	@NotNull(groups = { GROUP_6.class })
	private String validate_p7;
}
