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
package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ComboBox;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Form;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.domain.defn.extension.ActivateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.Audit;
import com.antheminc.oss.nimbus.domain.defn.extension.ConfigConditional;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreAuditEntry;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity.SampleForm;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreLevel1_Entity;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 * 
 */
@MapsTo.Type(SampleCoreEntity.class)
@Getter @Setter
public class VPSampleViewPageGreen {

	@Tile(size=Tile.Size.Large) 
	private TileGreen tile;	
	
	@MapsTo.Type(SampleCoreEntity.class)
	@Getter @Setter
	public static class TileGreen {
		@Section
		private SectionGrid section_grid;
		
		@Path("/attr_list_1_NestedEntity")
		private List<SampleCoreNestedEntity> list_attached_noConversion_NestedEntity;
		
		@Audit(SampleCoreAuditEntry.class)
		@Path
		private String audit_String; // mapped and marked with Audit on both view & core	
		
		@Audit(SampleViewAuditEntry.class)
		private String unmapped_String; // unmapped and marked with Audit on a quad with persistent core
		
		@Path
		private Integer audit_Integer; // mapped view to a core which is annotated with Audit
		
		@Path
		private SampleCoreLevel1_Entity level1;
		
		@ConfigConditional(config=@Config(url="/p/sample_view_audit_history/_new?fn=_initEntity&target=/domainRootRefId&json=<!/.m/id!>"))
		@Path
		private String for_mapped_state_change_attr;
		
		private VFSampleForm view_sample_form;
		
		@Section
		private VSFormWithGrid vsFormWithGrid;
    }
	
	@MapsTo.Type(SampleCoreEntity.class)
	@Getter @Setter
	public static class SectionGrid {
		
		//@Config(url="/pageHealthConcerns/tileHealthConcerns/sectionConcerns/gridConcerns.m/_process?fn=_set&url=/p/cmcase/_search?fn=query&where=cmcase.id.eq(<!/.m/id!>)&project=/healthProblemsEnclosed")
		@Path("/attr_list_2_NestedEntity")
		private List<ConvertedNestedEntity> grid_attached_ConvertedItems;
    }
	
	@MapsTo.Type(SampleCoreNestedEntity.class)
	@Getter @Setter
	public static class ConvertedNestedEntity {
		
		@Path
		private String nested_attr_String;
	}
	
	@MapsTo.Type(SampleCoreEntity.class)
	@Getter @Setter
	public static class VFSampleForm {
		
		@Path("/nc_form")
		private SampleForm view_nc_form;
		
		@ComboBox
		@Values(url = "a/b/p/sampletask/_search?fn=lookup&projection.mapsTo=code:id,label:taskName")
		private List<String> unsortedDynamicValues;
		
		@ComboBox
		@Values(url = "a/b/p/sampletask/_search?fn=lookup&orderby=sampletask.taskName.asc()&projection.mapsTo=code:id,label:taskName")
		private List<String> sortedDynamicValues;
	}
	
	@MapsTo.Type(SampleCoreEntity.class)
	@Getter @Setter
	public static class VSFormWithGrid {
		
		@Grid()
		@MapsTo.Path(linked = false)
		private List<ConvertedNestedEntity> testGrid;
		
		@Form(cssClass = "threeColumn")
		@Path(linked = false)
		private VFSearchForm vfSearchForm;
	}
	
	@MapsTo.Type(SampleCoreEntity.class)
	@Getter @Setter
	public static class VFSearchForm {
		
		@Path
		@TextBox(postEventOnChange=true)
		@ActivateConditional(when="state=='test'", targetPath="/../../testGrid")
		private String testEntry;
	}
	
}
