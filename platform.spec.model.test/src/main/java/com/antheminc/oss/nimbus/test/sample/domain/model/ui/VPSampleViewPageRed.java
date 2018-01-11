/**
 * 
 */
package com.antheminc.oss.nimbus.test.sample.domain.model.ui;

import com.antheminc.oss.nimbus.core.domain.definition.MapsTo;
import com.antheminc.oss.nimbus.core.domain.definition.Execution.Config;
import com.antheminc.oss.nimbus.core.domain.definition.Executions.Configs;
import com.antheminc.oss.nimbus.core.domain.definition.MapsTo.Nature;
import com.antheminc.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Tile;
import com.antheminc.oss.nimbus.test.sample.domain.model.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.sample.domain.model.core.SampleCoreNestedEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@MapsTo.Type(SampleCoreEntity.class)
@Getter @Setter
public class VPSampleViewPageRed {

	@Tile(title="Sample Page Red", size=Tile.Size.Large) 
	private TileRed tile;	
	
	@MapsTo.Type(SampleCoreEntity.class)
	@Getter @Setter
	public static class TileRed {
	
		// D. add/edit transient collection element (via Form converted-B)
		@Path(value="/attr_list_2_NestedEntity", nature=Nature.TransientColElem)
		private Form_ConvertedNestedEntity vt_attached_convertedNestedEntity;
	}
	
	@MapsTo.Type(SampleCoreNestedEntity.class)
	@Getter @Setter
	public static class Form_ConvertedNestedEntity {
		
		// detect if in add mode vs. edit: add results in addition to collection elements whereas edit updates existing element
		@Configs(
			@Config(url="<!#this!>/../_update")	
		)
		private String saveButton;
		
		// 1. delete mapsTo elem	2. unassign
		@Configs({
			@Config(url="<!#this!>/../.m/_delete"),
			@Config(url="<!#this!>/../_get?fn=param&expr=unassignMapsTo()")	
		})
		private String deleteButton;
		
		@Path("/nested_attr_String")
		private String vt_nested_attr_String;
	}
	
}
