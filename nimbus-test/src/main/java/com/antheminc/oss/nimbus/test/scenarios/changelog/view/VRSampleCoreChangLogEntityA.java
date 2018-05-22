package com.antheminc.oss.nimbus.test.scenarios.changelog.view;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Executions.Configs;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Button;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Form;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.test.scenarios.changelog.core.SampleCoreChangeLogEntityA;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value="samplechangelogview_a", includeListeners={ListenerType.websocket})
@MapsTo.Type(SampleCoreChangeLogEntityA.class)
@Repo(Database.rep_none)
@Getter @Setter
public class VRSampleCoreChangLogEntityA {
	
	@Config(url="/p/samplechangelogcore_b/_new?fn=_initEntity&target=/status&json=\"Test_Status_B\"&target=/sampleCoreChangLogEntityNestedB/nestedStatus&json=\"Test_Nested_Status_B\"")
	private String action_createEntityB;
	
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
	
	
	@MapsTo.Type(SampleCoreChangeLogEntityA.class)
	@Getter @Setter
	public static class VFSampleCoreChangeLog {
		
		@Path
		private String status;
		
		@Path
		private String statusSetViaRule;
		
		@Button
		@Configs({
			@Config(url="/.d/.m/sampleCoreChangLogEntityNested/nestedStatus/_update?rawPayload=\"Test_Nested_Status\""),
			@Config(url="/.d/_process?fn=_setByRule&&rule=stateless_changelog_entityA"),
			@Config(url="/p/samplechangelogview_b/_new?fn=_initEntity&target=/.m/status&json=\"Test_Status_B\""),
			
		})
		private String button;
		
	}
}
