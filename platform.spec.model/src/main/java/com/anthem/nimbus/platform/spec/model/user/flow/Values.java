/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.user.flow;

import java.util.ArrayList;
import java.util.List;

import com.anthem.nimbus.platform.spec.model.dsl.Model.Param.Values.Source;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamValue;

/**
 * @author Swetha Vemuri
 *
 */
public class Values {

	
	
	public static class ClientUserRole implements Source {
		
		
		/**
		 * 
		 */
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			
			values.add(new ParamValue("abcintake", "Intake"));
			values.add(new ParamValue("abcclinical", "Clinical"));
			values.add(new ParamValue("abcpcr", "Peer Clinical Review"));
			return values;
		}
	}
	
}
