/**
 *
 *  Copyright 2012-2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.anthem.nimbus.platform.web.flow.login;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Page;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Section;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.StaticText;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Tile;

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
