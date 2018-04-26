package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridColumn;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridColumn.FilterMode;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Link;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.LinkMenu;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.InnerGridModel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sandeep Mantha
 *
 */

@MapsTo.Type(InnerGridModel.class)
@Getter @Setter
public class InnerGridLineItem {
	
	@Path
	@GridColumn(hidden=true)
	private Long id;
	
	@Path()
	@GridColumn(filter = true, filterMode = FilterMode.contains)
	private String test_name;
	
	@LinkMenu
	private VLInnerLinks vlmCaseItemLinks;
	
	 @Model
	  @Getter @Setter
	  public static class VLInnerLinks {
		 	@Link
			private String innerLink1;
	
			@Link
			private String innerLink2;
	  }
}
