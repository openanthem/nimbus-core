/**
 *  Copyright 2016-2018 the original author or authors.
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

import java.util.ArrayList;
import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values.Source;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Radio;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditional.Condition;
import com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditionals;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;

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
			@ValuesConditional(targetPath = "../statusReason", resetOnChange = false, condition = { 
					@Condition(when = "state=='A'", then = @Values(SR_A.class)),
					@Condition(when = "state=='B'", then = @Values(SR_B.class)),
				}
			),
			@ValuesConditional(targetPath = "../statusReason2", resetOnChange = true, condition = { 
					@Condition(when = "state=='A'", then = @Values(SR_A.class)),
				}
			)
		})
		@TextBox(postEventOnChange = true)
		private String status;
		
		@ValuesConditional(targetPath = "../statusReason2", resetOnChange = false, condition= {
			@Condition(when = "state=='A'", then = @Values(SR_A.class))
		})
		private String status2;
		
		@ValuesConditional(targetPath = "../statusReason", condition = { 
				@Condition(when = "state=='A'", then = @Values(SR_A.class)),
				@Condition(when = "state=='A'", then = @Values(SR_B.class)),
			},
			exclusive = false	
		)
		@TextBox(postEventOnChange = true)
		private String allowOverrideStatus;
		
		@ValuesConditional(targetPath = "../statusReason", condition = { 
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
		
		@ValuesConditional(targetPath = "/../contactStatus", resetOnChange = false, condition = {
			@ValuesConditional.Condition(when = "state == 'changeit'", then = @Values(StatusPast.class))
		})
		@ValuesConditional(targetPath = "/../multiSelect", resetOnChange = false, condition = {
				@ValuesConditional.Condition(when = "state == null", then = @Values(StatusFuture.class)),
				@ValuesConditional.Condition(when = "state != null", then = @Values(StatusList.class))
			})
		private String contactType;
		
		@Values(StatusAll.class)
		private Status contactStatus;
		
		@Values(StatusAll.class)
		private String[] multiSelect;
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
	@Getter
	public static enum Status {
		CURRENT("Current"),
		PAST("Past");
		private String name;
		private Status(String name){
			this.name = name;
		}
	}
	public static class StatusAll implements Source {
		@Override
		public List<ParamValue> getValues(String paramCode) {
			final List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("PAST", "Past"));
			values.add(new ParamValue("CURRENT", "Current"));
			return values;
		}
	}
	public static class StatusPast implements Source {
		@Override
		public List<ParamValue> getValues(String paramCode) {
			final List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("PAST", "Past"));
			return values;
		}
	}
	
	public static class StatusFuture implements Source {
		@Override
		public List<ParamValue> getValues(String paramCode) {
			final List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("FUTURE", "Future"));
			return values;
		}
	}
	
	public static class StatusList implements Source {
		@Override
		public List<ParamValue> getValues(String paramCode) {
			final List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("FUTURE", "Future"));
			values.add(new ParamValue("PAST", "Past"));
			values.add(new ParamValue("CURRENT", "Current"));
			return values;
		}
	}
}
