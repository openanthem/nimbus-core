/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.user.role;

import java.util.ArrayList;
import java.util.List;

import com.anthem.nimbus.platform.spec.model.dsl.Model.Param.Values.Source;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamValue;

/**
 * @author Dinakar Meda
 *
 */
public class Values {

	
	
	public static class ClientID implements Source {
		
		
		/**
		 * 
		 */
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			
			values.add(new ParamValue("clid1", "Anthem"));
			values.add(new ParamValue("clid2", "AGP"));
			return values;
		}
	}
	
}
