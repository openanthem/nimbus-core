package com.antheminc.oss.nimbus.test.scenarios.s10.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TreeGrid;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Domain(value = "s10_view", includeListeners = { ListenerType.websocket })
@Repo(value = Database.rep_mongodb, cache = Cache.rep_device)
@Getter
@Setter
@ToString
public class S10_view extends AbstractEntity.IdLong{

	@Label("Owners")
	@Page(defaultPage = true)
	private VPOwners vpOwners;

	@Model
	@Getter
	@Setter
	public static class VPOwners {

		@Tile(imgSrc = "resources/icons/task.svg#Layer_1")
		private VTOwners vtOwners;
	}

	@Model
	@Getter
	@Setter
	public static class VTOwners {

		@Section
		private VSOwners vsOwners;
	}

	@Model
	@Getter
	@Setter
	public static class VSOwners {

		@Label("Owners")
		@TreeGrid
//		@Config(url = "<!#this!>/.m/_process?fn=_set&url=/p/owner/_search?fn=example")
		private List<OwnerLineItem> owners;
	}
}
