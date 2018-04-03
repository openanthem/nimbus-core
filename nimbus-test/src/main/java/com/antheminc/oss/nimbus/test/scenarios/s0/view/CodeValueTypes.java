package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import java.util.ArrayList;
import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values.Source;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;

/**
 * @author Sandeep Mantha
 *
 */

public class CodeValueTypes {

	public static class SampleDropDownValues implements Source {
		@Override
		public List<ParamValue> getValues(String paramPath) {
			List<ParamValue> values = new ArrayList<>();
			values.add(new ParamValue("view1", "view1"));
			values.add(new ParamValue("view2", "view2"));
			return values;
		}
	}

}
