/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
package com.anthem.oss.nimbus.test.sample.domain.model.core;

import java.util.ArrayList;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values;
import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values.Source;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Radio;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.TextBox;
import com.anthem.oss.nimbus.core.domain.definition.extension.ValuesConditional;
import com.anthem.oss.nimbus.core.domain.definition.extension.ValuesConditional.Condition;
import com.anthem.oss.nimbus.core.domain.definition.extension.ValuesConditionals;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;

import lombok.Getter;
import lombok.Setter;

@Model
@Getter @Setter
public class SampleCoreValuesEntity {
	
	private StatusForm statusForm;
	
	@Model
	@Getter @Setter 
	public static class StatusForm {
		
		@ValuesConditionals({
			@ValuesConditional(target = "../statusReason", resetOnChange = false, condition = { 
					@Condition(when = "state=='A'", then = @Values(SR_A.class)),
					@Condition(when = "state=='B'", then = @Values(SR_B.class)),
				}
			),
			@ValuesConditional(target = "../statusReason2", resetOnChange = true, condition = { 
					@Condition(when = "state=='A'", then = @Values(SR_A.class)),
				}
			)
		})
		@TextBox(postEventOnChange = true)
		private String status;
		
		@ValuesConditional(target = "../statusReason2", resetOnChange = false, condition= {
			@Condition(when = "state=='A'", then = @Values(SR_A.class))
		})
		private String status2;
		
		@ValuesConditional(target = "../statusReason", condition = { 
				@Condition(when = "state=='A'", then = @Values(SR_A.class)),
				@Condition(when = "state=='A'", then = @Values(SR_B.class)),
			},
			exclusive = false	
		)
		@TextBox(postEventOnChange = true)
		private String allowOverrideStatus;
		
		@ValuesConditional(target = "../statusReason", condition = { 
				@Condition(when = "state=='A'", then = @Values(SR_A.class)),
				@Condition(when = "state=='A'", then = @Values(SR_B.class)),
			}
		)
		@TextBox(postEventOnChange = true)
		private String disallowOverrideStatus;
		
		@Radio
		@Values(SR_DEFAULT.class)
		private String statusReason;
		
		@Radio
		private String statusReason2;
	}
	
	public static class SR_DEFAULT implements Source {
		@Override
		public List<ParamValue> getValues(String paramCode) {
			final List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("A1", "A1"));
			values.add(new ParamValue("B1", "B1"));
			return values;
		}
	}
	public static class SR_A implements Source {
		@Override
		public List<ParamValue> getValues(String paramCode) {
			final List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("A1", "A1"));
			return values;
		}
	}
	public static class SR_B implements Source {
		@Override
		public List<ParamValue> getValues(String paramCode) {
			final List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("B1", "B1"));
			return values;
		}
	}
}
