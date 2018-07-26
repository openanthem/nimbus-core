package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ComboBox;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Form;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.domain.defn.extension.ActivateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ActivateConditionals;
import com.antheminc.oss.nimbus.domain.defn.extension.MessageConditional;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.CodeValueTypes.SampleDropDownValues;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sandeep Mantha
 * @author Akancha Kashyap - added a new Section for Form Fields warning Validation
 *
 */

@Getter @Setter @Model
public class VPSampleViewPageOrange {

	@Tile(size = Tile.Size.Large)
	private VTOrange vtOrange;
	
	@Getter @Setter @Model
	public static class VTOrange {
		
		@Section
		private VSSwitchView vsSwitchView;
		
		@Section 
		private VSSampleGrid1 vsSampleGrid1;
		
		@Section
		private VSSampleGrid2 vsSampleGrid2;
		
		@Section
		private VSSampleForm vsSampleForm;
		
	}
	
	@Getter @Setter @Model
	public static class VSSwitchView {
		
		@ActivateConditionals({
			@ActivateConditional(when="state == 'view1'", targetPath="/../../vsSampleGrid1"), 
			@ActivateConditional(when="state == 'view2'", targetPath= "/../../vsSampleGrid2")	
		})
		@ComboBox(postEventOnChange=true)
		@Model.Param.Values(value = SampleDropDownValues.class)
		private String viewBy;
		
	}
	
	@Getter @Setter @Model
	public static class VSSampleGrid1 {
		
		@Grid(onLoad=true)
		@Path(linked=false)
		@Config(url = "/page_orange/vtOrange/vsSampleGrid1/sampleGrid1/.m/_process?fn=_set&url=/p/sample_core/_search?fn=query&where=sample_core.id.eq(1)")
		private List<SampleGrid1LineItem> sampleGrid1;
	}
	
	@Getter @Setter @Model
	public static class VSSampleGrid2 {
		
		@Grid(onLoad=true)
		@Path(linked=false)
		@Config(url = "/page_orange/vtOrange/vsSampleGrid2/sampleGrid2/.m/_process?fn=_set&url=/p/sample_core/_search?fn=query&where=sample_core.id.eq(1)")
		private List<SampleGrid2LineItem> sampleGrid2;
		
	}
	@Model @Getter @Setter
	public static class VSSampleForm{
		@Form
		private VFSampleForm vfSampleForm;
		
	}
	@Model @Getter @Setter
	public static class VFSampleForm{
		
		@TextBox(postEventOnChange=true)
		@MessageConditional(when="state =='Yes'", message="This is a Test Warning Message")
		private String testWarningTextBox;
		
		@TextBox(postEventOnChange=true)
		@MessageConditional(when="state == null", message="This is a Test Warning Message")
		private String testMessageTextBox2;
	}
}