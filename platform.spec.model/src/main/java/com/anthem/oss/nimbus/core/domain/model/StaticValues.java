/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.Model.Param.Values.Source;

/**
 * @author Sandeep Mantha
 *
 */
public class StaticValues {
	
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
