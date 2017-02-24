/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.um.model.view;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Link;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.TextBox;
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
public class Page_Main {

	@MapsTo.Type(ServiceLine.class)
	@Getter @Setter
	public static class V_CardServiceLine {
		
		@Path("service") private String header;
	}
	
	@MapsTo.Type(ServiceLine.class)
	@Getter @Setter
	public static class V_CRUDServiceLine {
		// TODO F/w change to get collection.elemId
		@TextBox(hidden=true)
		@Path private String elemId;
		
		@Path private String service;
		@Path private String something;
		
		@Link(url="/crudServiceLine/_process?b=$execute&stateUrl=/serviceLines/{elemId}") 
		private String crudServiceLine;
	}
	
	
	
	
	@Path("/serviceLines") 
	private List<V_CardServiceLine> vCardServiceLines;
	
	// MODE: null => unmapped(default), 
	// attached_final, attached_transient, detached  
	// attached=true(true)/false, temporal=true/false(default)
	
	@Path(linked=false, temporal=true)
	private V_CRUDServiceLine vCrudServiceLine;
}
