package com.antheminc.oss.nimbus.platform.web.flow.login;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Page;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Section;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Section.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Dinakar Meda
 *
 */
@Domain(value="loginlayout", includeListeners={ListenerType.websocket}) 
@Getter @Setter
public class VLLogin {
	@Model @Getter @Setter
	public static class VPLogin {
		
		@Section(Type.HEADER) 
		private VSLoginHeader vsLoginHeader;
		
		@Section(Type.FOOTER) 
		private VSLoginFooter vsLoginFooter;
		
	}
	
	@Page private VPLogin vpLogin;
}
