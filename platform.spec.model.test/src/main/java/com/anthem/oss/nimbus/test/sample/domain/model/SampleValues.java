/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values.Source;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;

/**
 * @author Soham Chakravarti
 *
 */
public class SampleValues {

    public static class YesNo implements Source {
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			
			values.add(new ParamValue("Y", "Yes"));
			values.add(new ParamValue("N", "No"));
			return values;
		}
	}
}
