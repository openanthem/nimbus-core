package com.antheminc.oss.nimbus.test.scenarios.s2.core;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Model;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Soham Chakravarti
 *
 */
@Domain("s2c_row")
@Getter @Setter
public class S2C_Row {

	@Model
	@Getter @Setter
	public static class Nested2LevelElem {
		
		private String nested2Value1;
	}
	
	@Model
	@Getter @Setter
	public static class NestedInRow {
		private String nestedValue1;
		private String nestedValue2;
		
		private List<Nested2LevelElem> nestedRowCollection;
	}
	
	private String topValue1;
	private String topValue2;
	
	private NestedInRow nestedInRow;
}
