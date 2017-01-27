/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.task;

import java.util.ArrayList;
import java.util.List;

import com.anthem.nimbus.platform.spec.model.dsl.Model.Param.Values.Source;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamValue;

/**
 * @author Sandeep Mantha
 *
 */
public class Values {

	
	
	public static class YesNo implements Source {
		

		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			
			values.add(new ParamValue("yes", "Yes"));
			values.add(new ParamValue("no", "No"));
			return values;
		}
	}
	
}
