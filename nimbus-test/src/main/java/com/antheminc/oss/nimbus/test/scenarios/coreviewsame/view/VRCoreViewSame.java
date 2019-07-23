/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.test.scenarios.coreviewsame.view;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Form;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Modal;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ViewRoot;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.CoreViewSameEntity;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.CoreViewSameEntity.PetCareForm;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tony Lopez
 *
 */
@Domain(value = "coreviewsame_view", includeListeners = { ListenerType.websocket })
@Repo(value = Database.rep_none, cache = Cache.rep_device)
@MapsTo.Type(CoreViewSameEntity.class)
@ViewRoot(layout = "home")
@Getter
@Setter
public class VRCoreViewSame {

	@Page(defaultPage = true)
	private VPMain vpMain;

	@Model
	@Getter
	@Setter
	public static class VPMain {

		@Tile
		private VTMain vtMain;
	}

	@Model
	@Getter
	@Setter
	public static class VTMain {

		@Label("Add Custom Note")
		@Modal(closable = true)
		private VMCustomNote vmCustomNote;

		@Label("Add System Note")
		@Modal(closable = true)
		private VMSystemNote vmSystemNote;

		@Section
		private VSMain vsMain;

	}

	@MapsTo.Type(CoreViewSameEntity.class)
	@Getter
	@Setter
	public static class VSMain {

		@Form(cssClass = "questionGroup")
		@Path
		private PetCareForm petCareForm;

	}

}
