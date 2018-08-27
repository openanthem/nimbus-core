package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Form;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.domain.defn.extension.MessageConditional;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleEntity;

import lombok.Getter;
import lombok.Setter;


/**
 * 
 * 
 ** @author Akancha Kashyap 
 */
@Getter @Setter @Model
public class VPSampleViewPageAqua {
	
	

		@Tile(size = Tile.Size.Large)
		private VTAqua vtAqua;
		
		@Getter @Setter @Model
		public static class VTAqua {
			
			
			
			@Section
			private VSSampleForm vsSampleForm;
			
		}
		
	
		
		@Model @Getter @Setter
		public static class VSSampleForm{
			@Form
			private VFSampleForm vfSampleForm;
			
		}
		@Model @Getter @Setter
		@MapsTo.Type(SampleCoreEntity.class)
		public static class VFSampleForm{
			
			@TextBox(postEventOnChange=true)
			@Path("/attr_int")
			@MessageConditional(when="state ==101", message="'This is a Test Warning Message'")
			private int testWarningTextBox;
			
			@TextBox(postEventOnChange=true)
			@MessageConditional(when="state == null", message="'This is a Test Warning Message'")
			private String testMessageTextBox2;
			
			@TextBox(postEventOnChange=true)
		
			@Path("/attr_String")
			@MessageConditional(when="state =='No'", message="findStateByPath('/../testWarningTextBox')")
			private String testWarningTextBox3;
		}
	}