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
package com.anthem.oss.nimbus.test.sample.um.model.core;

import java.util.ArrayList;
import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values.Source;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;

/**
 * @author AC12974
 *
 */
public class Values {

	
	
	public static class RequestType implements Source {

		
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			
			values.add(new ParamValue("inp", "Inpatient"));
			values.add(new ParamValue("outp", "Outpatient"));
			return values;
		}
	}
	
	
	
	public static class CaseType implements Source {
		

		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			
			values.add(new ParamValue("med", "Medical"));
			values.add(new ParamValue("surg", "Surgical"));
			return values;
		}
	}
	
	
	
    public static class YesNo implements Source {
		
    	
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			
			values.add(new ParamValue("yes", "Yes"));
			values.add(new ParamValue("no", "No"));
			return values;
		}
	}
	
    public static class Bothered implements Source {
		
    	
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			
			values.add(new ParamValue("notBot", "Not Bothered"));
			values.add(new ParamValue("BotALil", "Bothered a little"));
			values.add(new ParamValue("BotALot", "Bothered a lot"));
			return values;
		}
	}

    
	public static class PatientOutReach implements Source {

		
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			
			values.add(new ParamValue("comp", "Complete"));
			values.add(new ParamValue("incomp", "Incomplete"));
			return values;
		}
	}
	
	
    public static class Gender implements Source {
		
    	
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			
			values.add(new ParamValue("M", "Male"));
			values.add(new ParamValue("F", "Female"));
			return values;
		}
	}
	
}
