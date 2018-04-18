package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridColumn;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridColumn.FilterMode;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sandeep Mantha
 *
 */

@MapsTo.Type(SampleCoreEntity.class)
@Getter @Setter
public class SampleGrid1LineItem {
	
	@Path
	@GridColumn
	private Long id;
	
	@Path()
	@GridColumn(filter = true, filterMode = FilterMode.contains)
	private String attr_String;

}
