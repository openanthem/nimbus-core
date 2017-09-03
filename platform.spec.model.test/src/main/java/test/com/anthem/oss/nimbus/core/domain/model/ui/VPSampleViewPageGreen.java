/**
 * 
 */
package test.com.anthem.oss.nimbus.core.domain.model.ui;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Section;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Tile;

import lombok.Getter;
import lombok.Setter;
import test.com.anthem.oss.nimbus.core.domain.model.SampleCoreEntity;
import test.com.anthem.oss.nimbus.core.domain.model.SampleCoreNestedEntity;

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
	
	
}
