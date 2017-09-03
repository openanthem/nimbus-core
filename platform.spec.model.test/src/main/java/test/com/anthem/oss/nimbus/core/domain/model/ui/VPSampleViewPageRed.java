/**
 * 
 */
package test.com.anthem.oss.nimbus.core.domain.model.ui;

import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.definition.Execution.Config;
import com.anthem.oss.nimbus.core.domain.definition.Executions.Configs;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Nature;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Path;
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
public class VPSampleViewPageRed {

	@Tile(title="Sample Page Red", size=Tile.Size.Large) 
	private TileRed tile;	
	
	@MapsTo.Type(SampleCoreEntity.class)
	@Getter @Setter
	public static class TileRed {
	
		// D. add/edit transient collection element (via Form converted-B)
		@Path(value="/attr_list_2_NestedEntity", nature=Nature.TransientColElem)
		private Form_ConvertedNestedEntity vt_attached_convertedNestedEntity;
	}
	
	@MapsTo.Type(SampleCoreNestedEntity.class)
	@Getter @Setter
	public static class Form_ConvertedNestedEntity {
		
		// detect if in add mode vs. edit: add results in addition to collection elements whereas edit updates existing element
		@Configs(
			@Config(url="/sample_view/page_red/tile/vt_attached_convertedNestedEntity/_update")	
		)
		private String saveButton;
		
		@Path("/nested_attr_String")
		private String vt_nested_attr_String;
	}
	
}
