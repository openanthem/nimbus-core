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
package com.antheminc.oss.nimbus.entity.client;

import java.util.ArrayList;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values.Source;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;

/**
 * @author Sandeep Mantha
 *
 */
public class Values {

	public static class Status implements Source {
		/**
		 * 
		 */
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("selectOne", "Select One"));
			values.add(new ParamValue("OPEN", "Open"));
			values.add(new ParamValue("CLOSE", "Closed"));
			values.add(new ParamValue("PEND", "Pending"));
			
			return values;
		}
	}
	
	public static class GoalCategory implements Source {
		
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("selectOne", "Select One"));
			values.add(new ParamValue("Dietary", "Dietary"));
			values.add(new ParamValue("Safety", "Safety"));
			values.add(new ParamValue("Behavioral", "Behavioral"));
			values.add(new ParamValue("Nursing", "Nursing"));
			values.add(new ParamValue("Physiotherapy", "Physiotherapy"));
			
			return values;
		}
	}
	
	public static class GoalStatus implements Source {
		
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("selectOne", "Select One"));
			values.add(new ParamValue("Proposed", "Proposed"));
			values.add(new ParamValue("Planned", "Planned"));
			values.add(new ParamValue("Accepted", "Accepted"));
			values.add(new ParamValue("Rejected", "Rejected"));
			values.add(new ParamValue("InProgress", "InProgress"));
			values.add(new ParamValue("Achieved", "Achieved"));
			values.add(new ParamValue("Sustaining", "Sustaining"));
			values.add(new ParamValue("OnHold", "OnHold"));
			values.add(new ParamValue("Cancelled", "Cancelled"));
			
			return values;
		}
	}
	
	public static class GoalIntention implements Source {
		
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("selectOne", "Select One"));
			values.add(new ParamValue("Achieve", "Achieve"));
			values.add(new ParamValue("Maintain", "Maintain"));
			values.add(new ParamValue("Avoid", "Avoid"));
			values.add(new ParamValue("Manage", "Manage"));
			
			return values;
		}
	}
	
	public static class BusinessType implements Source {

		
		/**
		 * 
		 */
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("selectOne", "Select One"));
			values.add(new ParamValue("caseMng", "CaseManagement"));
			return values;
		}
	}
	
public static class BarrierType implements Source {
		
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			
			values.add(new ParamValue("select one", "Select One"));
			values.add(new ParamValue("Financial Stress", "Financial Stress"));
			values.add(new ParamValue("Functional Limitation", " Functional Limitation"));
			values.add(new ParamValue("Psycosocial", "Psycosocial"));
			values.add(new ParamValue("Resource or Service Scheduling", "Resource or Service Scheduling"));
			values.add(new ParamValue("Dependency on others", "Dependency on others"));
			values.add(new ParamValue("Language", "Language"));
			values.add(new ParamValue("Transportation", "Transportation"));
			return values;
		}
   }

}
