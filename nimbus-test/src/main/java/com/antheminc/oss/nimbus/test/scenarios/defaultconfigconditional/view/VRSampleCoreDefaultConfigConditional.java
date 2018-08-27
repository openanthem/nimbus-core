package com.antheminc.oss.nimbus.test.scenarios.defaultconfigconditional.view;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Execution.DetourConfig;
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
	
	@DetourConfig(main = @Config(when = "findStateByPath(\"/.d/.m12/status\") == \"A\"", url = "/.d/.m/status/_update?rawPayload=\"<!/.d/.m/status!>B\""), 
			onException = @Config(url = "/.d/.m/status/_update?rawPayload=\"<!/.d/.m/status!>pq\""), order = 1)
	@Config(when="findStateByPath(\"/.d/.m/status\") == \"Apq\"", url="/.d/.m/status/_update?rawPayload=\"<!/.d/.m/status!>B\"", order=2)
	private String upateStatusWithDefaultConfigConditional_true;
	
	@Config(when="findStateByPath(\"/.d/.m/status\") == \"ApqB\"", url="/.d/.m/status/_update?rawPayload=\"<!/.d/.m/status!>D\"")
	private String upateStatusWithDefaultConfigConditional_false;

}
