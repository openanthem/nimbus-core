package com.antheminc.oss.nimbus.test.sample.domain.model.core;

import java.util.ArrayList;
import java.util.List;

import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.domain.definition.Model.Param.Values;
import com.antheminc.oss.nimbus.core.domain.definition.Model.Param.Values.Source;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Radio;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.core.domain.definition.extension.ValuesConditional;
import com.antheminc.oss.nimbus.core.domain.definition.extension.ValuesConditional.Condition;
import com.antheminc.oss.nimbus.core.domain.definition.extension.ValuesConditionals;
import com.antheminc.oss.nimbus.core.domain.model.config.ParamValue;

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
