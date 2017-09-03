/**
 * 
 */
package test.com.anthem.oss.nimbus.core.domain.model.ui;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Execution.Config;
import com.anthem.oss.nimbus.core.domain.definition.Executions.Configs;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
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
public class VPSampleViewPageBlue {


	@Tile(title="Sample Page Blue", size=Tile.Size.Large) 
	private TileBlue tile;	
	
	@MapsTo.Type(SampleCoreEntity.class)
	@Getter @Setter
	public static class TileBlue {
		
		// A. add new Nested Entity (via Form converted-A)
		@Configs(
			@Config(url="/page_red/tile/vt_attached_convertedNestedEntity/_get?fn=param&expr=assignMapsTo(getParentModel().findParamByPath('/.m/attr_list_2_NestedEntity'))")	
		)
		private String addButton;

		// B. view/edit mapped collection to core (via converted-B)
		@Path(value="/attr_list_2_NestedEntity")
		private List<Section_ConvertedNestedEntity> vm_attached_convertedList;
		
		// B. show drop down code-value to core
		//==TODO

    }
	
	@MapsTo.Type(SampleCoreNestedEntity.class)
	@Getter @Setter
	public static class Section_ConvertedNestedEntity {
		
		// view/edit link to Form converted-A: by assigning collection element of core to Form
		@Configs(
			@Config(url="/page_red/tile/vt_attached_convertedNestedEntity/_get?fn=param&expr=assignMapsTo('/.m')")	
		)
		private String editButton;
		
		@Path("/nested_attr_String")
		private String vm_nested_attr_String;
	}

}
