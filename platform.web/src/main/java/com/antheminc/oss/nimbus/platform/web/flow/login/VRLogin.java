package com.antheminc.oss.nimbus.platform.web.flow.login;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Page;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Section;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.StaticText;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Tile;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Dinakar Meda
 *
 */
@Domain(value="login", includeListeners={ListenerType.websocket}) 
@Getter @Setter
public class VRLogin {
	@Page
	private PageLogin pageLogin;
	
	@Model
	@Getter @Setter
	public static class PageLogin  {

		@Tile(title="LOGIN", imgSrc="resources/icons/member-2.svg#Layer_1", size=Tile.Size.Large) 
		private CardLogin cardLogin;
	}
	
	@Model
	@Getter @Setter
	public static class CardLogin  {

		@Section
		private SectionLogin sectionLogin;
    }
	
	@Model
	@Getter @Setter
	public static class SectionLogin {

		@StaticText(contentId="testContent") 
		private String verbiage;
	}

}
