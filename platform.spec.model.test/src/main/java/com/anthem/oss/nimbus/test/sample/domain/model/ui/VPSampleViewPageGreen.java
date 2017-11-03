/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.domain.model.ui;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Execution.Config;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Section;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Tile;
import com.anthem.oss.nimbus.core.domain.definition.extension.Audit;
import com.anthem.oss.nimbus.core.domain.definition.extension.ConfigConditional;
import com.anthem.oss.nimbus.test.sample.domain.model.core.SampleCoreAuditEntry;
import com.anthem.oss.nimbus.test.sample.domain.model.core.SampleCoreEntity;
import com.anthem.oss.nimbus.test.sample.domain.model.core.SampleCoreEntity.SampleForm;
import com.anthem.oss.nimbus.test.sample.domain.model.core.SampleCoreLevel1_Entity;
import com.anthem.oss.nimbus.test.sample.domain.model.core.SampleCoreNestedEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 * 
 */
@MapsTo.Type(SampleCoreEntity.class)
@Getter @Setter
public class VPSampleViewPageGreen {

	@Tile(title="Sample Page Green", size=Tile.Size.Large) 
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
		
		@ConfigConditional(config=@Config(url="/p/sample_view_audit_history/_new?fn=_initEntity&target=/domainRootRefId&json=\"<!/.m/id!>\""))
		@Path
		private String for_mapped_state_change_attr;
		
		private VFSampleForm view_sample_form;
    }
	
	@MapsTo.Type(SampleCoreEntity.class)
	@Getter @Setter
	public static class SectionGrid {
		
		//@Config(url="/pageHealthConcerns/tileHealthConcerns/sectionConcerns/gridConcerns.m/_process?fn=_set&url=/p/cmcase/_search?fn=query&where=cmcase.id.eq('<!/.m/id!>')&project=/healthProblemsEnclosed")
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
	}
	
}
