package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Form;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.defn.extension.LabelConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.LabelConditional.Condition;
import com.antheminc.oss.nimbus.domain.defn.extension.MessageConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.Rule;
import com.antheminc.oss.nimbus.domain.defn.extension.Style;
import com.antheminc.oss.nimbus.domain.defn.extension.StyleConditional;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;

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
			
			@Form
			private VFStyleForm vfStyleForm;
			
		}
		
		@Model @Getter @Setter
		@MapsTo.Type(SampleCoreEntity.class)
		public static class VFSampleForm{
			
			@TextBox(postEventOnChange=true)
			@Path("/attr_int")
			@MessageConditional(when = "state == 101", message = "'This is a Test Warning Message'")
			@LabelConditional(targetPath = "/../testMessageTextBox2", condition = {
				@Condition(when = "state == 102", then = @Label(value = "Awesome Textbox 2")),
				@Condition(when = "state == 103", then = @Label(value = "Super Awesome Textbox 2"))
			})
			private int testWarningTextBox;
			
			@Label("Textbox 2")
			@TextBox(postEventOnChange=true)
			@MessageConditional(when="state == null", message="'This is a Test Warning Message'")
			private String testMessageTextBox2;
			
			@TextBox(postEventOnChange=true)
			@Path("/attr_String")
			@MessageConditional(when="state =='No'", message="findStateByPath('/../testWarningTextBox')")
			private String testWarningTextBox3;
			
			@TextBox(postEventOnChange = true)
			@LabelConditional(targetPath = "/", condition = {
				@Condition(when = "state == 'test-label-conditional-1'", then = @Label(value = "Amazing Label")),
				@Condition(when = "state == 'test-label-conditional-2'", then = @Label(value = "Hello <!/../p5!>!"))
			})
			private String p4;
			
			private String p5;
			
			@LabelConditional(targetPath = "/", exclusive = true, condition = {
				@Condition(when = "state > 5", then = @Label(value = "I'm greater than 5!")),
				@Condition(when = "state > 10", then = @Label(value = "I'm greater than 10!"))
			})
			private int p6_exclusive;
			
			@LabelConditional(targetPath = "/", exclusive = false, condition = {
				@Condition(when = "state > 5", then = @Label(value = "I'm greater than 5!")),
				@Condition(when = "state > 10", then = @Label(value = "I'm greater than 10!"))
			})
			private int p6_nonExclusive;
			
			@MessageConditional(when="state =='Set'", targetPath="/../p8_message", message="'Message is set on p8_message'")
			private String p7_messagetest;
			
			private String p8_message;
			
			@Rule("p9_message_onload")
			private String p9_message;
			
		}
		
		@Model
		@Getter @Setter
		public static class VFStyleForm {
			
			private String p0;
			
			@StyleConditional(targetPath = "/../p0", condition = {
				@StyleConditional.Condition(when = "state == 1", then = @Style(cssClass = "red"))
			})
			private int p1;
			
			@StyleConditional(targetPath = "/../p0", condition = {
				@StyleConditional.Condition(when = "state == 1", then = @Style(cssClass = "red")),
				@StyleConditional.Condition(when = "state == 2", then = @Style(cssClass = "blue")),
				@StyleConditional.Condition(when = "state == 3", then = @Style(cssClass = "green"))
			})
			private int p2;
			
			@StyleConditional(targetPath = "/", exclusive = true, condition = {
				@StyleConditional.Condition(when = "state > 5", then = @Style(cssClass = "red")),
				@StyleConditional.Condition(when = "state > 10", then = @Style(cssClass = "green"))
			})
			private int p3Exclusive;
			
			@StyleConditional(targetPath = "/", exclusive = false, condition = {
				@StyleConditional.Condition(when = "state > 5", then = @Style(cssClass = "red")),
				@StyleConditional.Condition(when = "state > 10", then = @Style(cssClass = "green"))
			})
			private int p3NonExclusive;
		}
	}