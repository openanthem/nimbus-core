/**
 * 
 */
package com.antheminc.oss.nimbus.test.sample.um.model.view;

import java.util.List;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.MapsTo;
import com.antheminc.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Page;
import com.antheminc.oss.nimbus.test.sample.um.model.core.ServiceLine;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="v_um_dashboard",includeListeners={ListenerType.websocket})
@Getter @Setter
public class V_UMDashboard {

	@MapsTo.Type(ServiceLine.class)
	@Getter @Setter
	public static class V_CardServiceLine {
		
		private String service;
	}
	
	@Model
	@Getter @Setter
	public static class Section_Main {
		
		@Path(linked=false)
		private List<V_CardServiceLine> serviceLines;
	} 
	
	@Model
	@Getter @Setter
	public static class Page_Landing {
		
		private Section_Main main;
	}
	
	@Page
	private Page_Landing pgLanding;
}
