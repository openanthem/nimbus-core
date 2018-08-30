/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.labelstate.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Type;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.defn.extension.Contents.Labels;
import com.antheminc.oss.nimbus.domain.defn.extension.Style;
import com.antheminc.oss.nimbus.test.scenarios.labelstate.core.Sample_Core_Label_Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Swetha Vemuri
 *
 */
@Domain("sample_view_label") @Type(Sample_Core_Label_Entity.class)
@Repo(value=Database.rep_none, cache=Cache.rep_device)
@Getter @Setter @ToString
public class Sample_View_Label_Entity {

	private String label_empty;
	
	@Label("Test Label A")
	private String label_a_en;
	
	@Label(value="Test Label B in French", localeLanguageTag="fr", helpText="some tooltip text here B")
	private String label_b_fr;
	
	@Label(value="Test Label C in English", helpText="some tooltip text here C")
	@Label(value="Test Label A in French", localeLanguageTag="fr")
	private String label_c_multiple;
	
	@Label(value = "Test Label D in English", style = @Style(cssClass=" foo bar "))
	private String label_d_styles;
		
	@Grid
	@Label("Test Grid Label")
	private List<Sample_View_Nested_Label> label_nested_coll_1; 
	
	@Grid
	@Labels({
		@Label("Test Grid Label en"),
		@Label(value="Test Grid Label fr",localeLanguageTag="fr")
	})
	private List<Sample_View_Nested_Label> label_nested_coll_2; 
	
	@Model @Getter @Setter
	public static class Sample_View_Nested_Label {
		
		@Label("Test GridLineItem label")
		private String label_nested_attr1;
	}
}
