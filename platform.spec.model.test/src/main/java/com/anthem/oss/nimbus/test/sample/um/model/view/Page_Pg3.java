/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.um.model.view;

import java.util.List;

import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo.Path;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Button;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Hints;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Hints.AlignOptions;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.TextBox;
import com.anthem.oss.nimbus.test.sample.um.model.ServiceLine;
import com.anthem.oss.nimbus.test.sample.um.model.UMCase;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@MapsTo.Type(UMCase.class)
@Getter @Setter
public class Page_Pg3 {

	private String mappedCaseId;
	
	@MapsTo.Type(ServiceLine.class)
	@Getter @Setter
	public static class Section_ServiceLine {
		
		private String someViewOnlyParam;
		
		@Path private String service;
	}
	
	@Path("/caseType") private @TextBox String aloha;

	@Button(url="/_nav?a=back") @Hints(align=AlignOptions.Left)
	private String back;
	
	
	@Path("/oneServiceLine") private ServiceLine coreAttachedOneServiceLine;
	@Path(linked=false) private ServiceLine noConversionDetachedOneServiceLine;
	
	@Path("/oneServiceLineConverted") private Section_ServiceLine viewAttachedOneServiceLineConverted;
	@Path(linked=false) private Section_ServiceLine convertedDetachedOneServiceLine;
	
	
	// collection mapping 1: primitive list direct mapping
	@Path private List<String> types;

	// collection mapping 2: List of CoreModel mapping directly to List of CoreModel
	@Path("/serviceLines") private List<ServiceLine> noConversionAttachedColServiceLines;
	//@Path(linked=false) private List<ServiceLine> noConversionDetachedColServiceLines;

	
	// collection mapping 4: ATTACHED: List of ViewModel mapping to List of CoreModel: ViewModel is mapped to CoreModel
	@Path("/serviceLinesConverted") private List<Section_ServiceLine> viewAttachedServiceLinesConverted;
	
	// collection mapping 5: DEATACHED: List of ViewModel mapping to List of CoreModel: ViewModel is mapped to CoreModel
	//@Path(linked=false) private List<Section_ServiceLine> viewDetachedServiceLines;

	// collection mapping 6: ATTACHED: List of primitive mapping to one param inside List elem of CoreModel
	//@Path("/serviceLines/{index}/service") List<String> attachedNestedAttribServices;
	
	// collection mapping 7: DETACHED: List of primitive mapping to one param inside List elem of CoreModel
	//@Path(value="/serviceLines/{index}/service", linked=false) List<String> detachedNestedAttribServices;
}
