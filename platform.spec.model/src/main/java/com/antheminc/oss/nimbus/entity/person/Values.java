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
package com.antheminc.oss.nimbus.entity.person;

import java.util.ArrayList;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values.Source;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;

/**
 * @author Sandeep Mantha
 *
 */
public class Values {

	
	
	public static class AddressType implements Source {
		
		
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue(Address.Type.BILLING.name(), Address.Type.BILLING.name()));
			values.add(new ParamValue(Address.Type.MAILING.name(), Address.Type.MAILING.name()));
			return values;
		}
	}
	

	
	public static class Country implements Source {
		
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("selectOne", "Select One"));
			values.add(new ParamValue("usa", "USA"));
			return values;
		}
	}
	
}
