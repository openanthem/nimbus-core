/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.s2.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Type;
import com.antheminc.oss.nimbus.test.scenarios.s2.core.S2C_Row;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Domain("s2v_main")
@Getter @Setter
public class S2V_VRMain {
	/*
	 * 1. NestedCollection.elementConfig is different than what is sent in ParamConfig
	 * 		- DONE: rename attribs = type, model, params, param inside Config to end with 'config  
	 * 2. ElementConfig doesnt send nested model config
	 * 3. Setting of value to nested collection within row emits entire outer collection as update 
	 */
	
	
	@Type(S2C_Row.Nested2LevelElem.class)
	@Getter @Setter
	public static class V_Nested2LevelElem {
		@Path
		private String nested2Value1;
	}
	
	@Type(S2C_Row.NestedInRow.class)
	@Getter @Setter
	public static class V_NestedInRow {
		@Path
		private String nestedValue1;
		
		@Path
		private String nestedValue2;
		
		@Path
		private List<V_Nested2LevelElem> nestedRowCollection;
	}
	
	@Type(S2C_Row.class)
	@Getter @Setter
	public static class V_Row {
		@Path
		private String topValue1;
		
		@Path
		private String topValue2;
		
		@Path
		private V_NestedInRow nestedInRow;
	}
	
	@Path(linked=false)
	private List<V_Row> rows;
}
