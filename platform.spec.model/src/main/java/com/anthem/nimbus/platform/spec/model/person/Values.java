/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.person;

import java.util.ArrayList;
import java.util.List;

import com.anthem.nimbus.platform.spec.model.dsl.Model.Param.Values.Source;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamValue;

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
