package com.anthem.nimbus.platform.web.flow.login;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Page;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Section;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Section.Type;

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
