package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridColumn;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridColumn.FilterMode;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridRowBody;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Link;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.LinkMenu;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sandeep Mantha
 *
 */

@MapsTo.Type(SampleCoreEntity.class)
@Getter @Setter
public class SampleGrid2LineItem {

	@Path()
	@GridColumn
	private Long id;
	
	@Path()
	@GridColumn(filter = true, filterMode = FilterMode.contains)
	private String attr_String;
	
	@GridRowBody
	private RowBody childList;
	
	@LinkMenu
	private VLLinks vlmCaseItemLinks;
	
	@MapsTo.Type(SampleCoreEntity.class)
	@Getter @Setter
	public static class RowBody {
		
		@MapsTo.Path(linked = false)
		@Grid(onLoad = true, pagination=false, showHeader=false)
		@Config(url = "/<!#this!>/.m/_process?fn=_set&url=/p/sample_entity/_search?fn=query&where=sample_entity.id.eq(1)")
		private List<InnerGridLineItem> innerGrid;
		
	}
	
	 @Model
	  @Getter @Setter
	  public static class VLLinks {
		 	@Link
			private String link1;
	
			@Link()
			private String link2;
	  }
}
