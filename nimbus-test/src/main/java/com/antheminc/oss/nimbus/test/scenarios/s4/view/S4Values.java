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
package com.antheminc.oss.nimbus.test.scenarios.s4.view;

import java.util.ArrayList;
import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values.Source;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;

/**
 * @author Tony Lopez
 *
 */
public class S4Values {

	public static class Q1_Preferences implements Source {
		/* (non-Javadoc)
		 * @see com.antheminc.oss.nimbus.domain.defn.Model.Param.Values.Source#getValues(java.lang.String)
		 */
		@Override
		public List<ParamValue> getValues(String paramCode) {
			List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("preference1", "Preference 1"));
			values.add(new ParamValue("preference2", "Preference 2"));
			return values;
		}
	}
	
	public static class Preference1Types implements Source {
		/* (non-Javadoc)
		 * @see com.antheminc.oss.nimbus.domain.defn.Model.Param.Values.Source#getValues(java.lang.String)
		 */
		@Override
		public List<ParamValue> getValues(String paramCode) {
			List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("preference1a", "Preference 1A"));
			values.add(new ParamValue("preference1b", "Preference 1B"));
			values.add(new ParamValue("preference1c", "Preference 1C"));
			return values;
		}
	}
	
	public static class Preference2Types implements Source {
		/* (non-Javadoc)
		 * @see com.antheminc.oss.nimbus.domain.defn.Model.Param.Values.Source#getValues(java.lang.String)
		 */
		@Override
		public List<ParamValue> getValues(String paramCode) {
			List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("preference2a", "Preference 2A"));
			values.add(new ParamValue("preference2b", "Preference 2B"));
			values.add(new ParamValue("preference2c", "Preference 2C"));
			return values;
		}
	}
}
