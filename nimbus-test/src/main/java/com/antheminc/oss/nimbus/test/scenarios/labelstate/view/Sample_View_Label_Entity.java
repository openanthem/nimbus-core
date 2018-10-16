/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.test.scenarios.labelstate.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
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
	
	@Label("Test Label E")
	private Sample_View_Nested_Label label_nested;
		
	@Grid
	@Label("Test Grid Label")
	private List<Sample_View_Nested_Label> label_nested_coll_1; 
	
	@Grid
	@Labels({
		@Label("Test Grid Label en"),
		@Label(value="Test Grid Label fr",localeLanguageTag="fr")
	})
	private List<Sample_View_Nested_Label> label_nested_coll_2; 
	
	@Label("T1")
	@Label("T2")
	private String same_locale_multiple;
	
	@Label(value="This label color is <!../label_replace!>", helpText="some help text in <!../label_replace!>")
	private String label_dynamic_a;
	
	@Label("This label color is <!../invalid_param!>")
	private String label_dynamic_negative;
	
	@Path("/attr1")
	private String label_replace;
	
	@Model @Getter @Setter
	public static class Sample_View_Nested_Label {
		
		@Label("Test Label Nested attr1")
		private String label_nested_attr1;
		
		@Label(value="Test Label Nested attr2",helpText="some nested attt2 help text")
		@Label(value="Test Label Nested attr2 fr",localeLanguageTag="fr")
		private String label_nested_attr2;
		
		private Sample_View_Entity_2 label_nested_level_2;
	}
	
	@Model @Getter @Setter
	public static class Sample_View_Entity_2 {
		
		@Label("Test Label Nested level 2 attr")
		private String label_nested_attr_level2;		
	}
}
