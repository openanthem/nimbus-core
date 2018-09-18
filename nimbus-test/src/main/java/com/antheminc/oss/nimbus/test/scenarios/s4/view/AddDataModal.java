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
package com.antheminc.oss.nimbus.test.scenarios.s4.view;

import javax.validation.constraints.NotNull;

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Executions.Configs;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Nature;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Button;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Button.Style;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ComboBox;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Form;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditional.Condition;
import com.antheminc.oss.nimbus.test.scenarios.s4.core.MyData;
import com.antheminc.oss.nimbus.test.scenarios.s4.core.MyData.Preference;
import com.antheminc.oss.nimbus.test.scenarios.s4.core.S4_MainCoreBackingObject;
import com.antheminc.oss.nimbus.test.scenarios.s4.view.S4Values.Preference1Types;
import com.antheminc.oss.nimbus.test.scenarios.s4.view.S4Values.Preference2Types;
import com.antheminc.oss.nimbus.test.scenarios.s4.view.S4Values.Q1_Preferences;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tony Lopez
 *
 */
@Model @Getter @Setter
public class AddDataModal {
	
	@Section
	private VSAddDataModal vsAddDataModalBody;
	
	@MapsTo.Type(S4_MainCoreBackingObject.class)
	@Model @Getter @Setter
	public static class VSAddDataModal {
		
		@Form
		@MapsTo.Path(value = "/myDataCollection", nature = Nature.TransientColElem)
		private VFAddDataModalForm vfAddDataModalForm;
	}
	
	@MapsTo.Type(MyData.class)
	@Model @Getter @Setter
	public static class VFAddDataModalForm {
		
		@NotNull
		@ValuesConditional(targetPath = "/../q2", resetOnChange = false, condition = {
			@Condition(when = "state!=null && state.name()=='preference1'", then=@Values(Preference1Types.class)),
			@Condition(when = "state!=null && state.name()=='preference2'", then=@Values(Preference2Types.class))
		})
		@ComboBox(postEventOnChange = true)
		@Values(Q1_Preferences.class)
		@Label("Which do you prefer?")
		@MapsTo.Path
		private Preference q1;
		
		@NotNull
		@ComboBox
		@Label("Given the answer from question #1, which of these do you prefer?")
		@MapsTo.Path
		private String q2;
		
		@Button(style = Style.PRIMARY, type = Button.Type.submit)
		@Label("Save Data")
		@Configs({
			@Config(url="<!#this!>/../_replace"),
			//@Config(url = "/vpMain/vtMain/addDataModal/_process?fn=_setByRule&rule=togglemodal")
		})					
		private String saveData;
		
		@Button
		@Label("Cancel")
		//@Config(url = "/vpMain/vtMain/addDataModal/_process?fn=_setByRule&rule=togglemodal")
		private String cancel;
	}
}