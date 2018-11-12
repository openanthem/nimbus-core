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
package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import java.util.ArrayList;
import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values.Source;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ComboBox;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tony Lopez
 *
 */
@Model
@Getter @Setter
public class VPSampleViewPageValues {

	@Tile
	private VT vt;
	
	@Model
	@Getter @Setter
	public static class VT {
		
		@Section
		private VS vs;
	}
	
	@Model
	@Getter @Setter
	public static class VS {
		
		@ComboBox
		@Values(SampleValues1.class)
		private String comboboxSingleValues;
	}
	
	public static class SampleValues1 implements Source {
		@Override
		public List<ParamValue> getValues(String paramCode) {
			List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("PVA1", "PVA1"));
			return values;
		}
	}
}
