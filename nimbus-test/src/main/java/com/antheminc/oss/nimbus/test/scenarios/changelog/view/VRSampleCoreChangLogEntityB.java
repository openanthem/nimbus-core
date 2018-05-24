package com.antheminc.oss.nimbus.test.scenarios.changelog.view;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Form;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.test.scenarios.changelog.core.SampleCoreChangeLogEntityB;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value="samplechangelogview_b", includeListeners={ListenerType.websocket})
@MapsTo.Type(SampleCoreChangeLogEntityB.class)
@Repo(Database.rep_none)
@Getter @Setter
public class VRSampleCoreChangLogEntityB {
	
	@Page
	private VPSampleCoreChangeLog vpSampleCoreChangeLog;
	
	
	@Model
	@Getter @Setter
	public static class VPSampleCoreChangeLog {
		
		@Tile
		private VTSampleCoreChangeLog vtSampleCoreChangeLog;
	}
	
	
	@Model
	@Getter @Setter
	public static class VTSampleCoreChangeLog {
		
		@Section
		private VSSampleCoreChangeLog vsSampleCoreChangeLog;
	}
	
	
	@Model
	@Getter @Setter
	public static class VSSampleCoreChangeLog {
	
		@Form
		private VFSampleCoreChangeLog vfSampleCoreChangeLog;
	}
	
	
	@MapsTo.Type(SampleCoreChangeLogEntityB.class)
	@Getter @Setter
	public static class VFSampleCoreChangeLog {
		
		@Path
		private String status;
	}
}
