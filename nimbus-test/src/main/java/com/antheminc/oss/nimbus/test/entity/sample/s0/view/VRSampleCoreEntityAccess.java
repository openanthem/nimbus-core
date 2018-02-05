package com.antheminc.oss.nimbus.test.entity.sample.s0.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Type;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.test.entity.sample.s0.core.SampleCoreEntityAccess;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value = "sample_core_access_view", includeListeners = { ListenerType.websocket })
@Type(SampleCoreEntityAccess.class)
//@ViewRoot(layout = "")
@Repo(value = Database.rep_none, cache = Cache.rep_device)
@Getter @Setter
public class VRSampleCoreEntityAccess {

	@Page(route="sample_core_access_view")
	private VPSampleCoreEntityAccess vpSampleCoreEntityAccess;
	
	@Model @Getter @Setter
	public static class VPSampleCoreEntityAccess {
		
		@Tile
		private VTSampleCoreEntityAccess vtSampleCoreEntityAccess;
		
	}
	
	@Model @Getter @Setter
	public static class VTSampleCoreEntityAccess {
		
		@Section
		private VSSampleCoreEntityAccess vsSampleCoreEntityAccess;
	}
	
	@Model @Getter @Setter
	public static class VSSampleCoreEntityAccess {
		
		@MapsTo.Path(linked = false)
		@Config(url = "/vpSampleCoreEntityAccess/vtSampleCoreEntityAccess/vsSampleCoreEntityAccess/vgSampleCoreEntities.m/_process?fn=_set&url=/p/sample_core_access/_search?fn=example")
		@Grid(onLoad=true)
		private List<SampleCoreEntityAccessLineItem> vgSampleCoreEntities;
	}
	
	

}