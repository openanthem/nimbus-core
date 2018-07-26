package com.antheminc.oss.nimbus.test.scenarios.defaultconfigconditional.view;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.test.scenarios.defaultconfigconditional.core.SampleCoreDefaultConfigConditional;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value="sampledefaultconfigconditionalview", includeListeners={ListenerType.websocket})
@MapsTo.Type(SampleCoreDefaultConfigConditional.class)
@Repo(Database.rep_none)
@Getter @Setter
public class VRSampleCoreDefaultConfigConditional {
	
	@Config(url="/.d/.m/status/_update?rawPayload=\"A\"")
	private String upateStatusWithDefaultConfig;
	
	@Config(when="findStateByPath(\"/.d/.m/status\") == \"A\"", url="/.d/.m/status/_update?rawPayload=\"<!/.d/.m/status!>B\"")
	private String upateStatusWithDefaultConfigConditional_true;
	
	@Config(when="findStateByPath(\"/.d/.m/status\") == \"C\"", url="/.d/.m/status/_update?rawPayload=\"<!/.d/.m/status!>D\"")
	private String upateStatusWithDefaultConfigConditional_false;

}
