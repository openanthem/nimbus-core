/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Executions.Configs;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ViewRoot;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Sandeep Mantha
 *
 */
@Domain(value="samplebpmn", includeListeners={ListenerType.websocket}, lifecycle = "samplebpmn")
@ViewRoot(layout = "")
@Repo(value = Repo.Database.rep_none, cache = Repo.Cache.rep_device)
@Getter @Setter
@MapsTo.Type(SampleCoreEntity.class)
public class VRSampleWithBpmn {
	
	private String attr_task1;

	private String attr_task2;

	private String attr_evalbpmn;
	
	private String currentTaskName;
	
	@Configs({
		@Config(url = "/p/sampletaskview/_new?fn=_initEntity&target=/.m/taskName&json=\"task1\"&target=/.m/entityId&json=<!/../.m/id!>"),
		@Config(url = "/currentTaskName/_update?rawPayload=\"task1\"")
	})
	private String action_createTempTask;
	
	@Configs({
		@Config(url = "/p/sampletask2view/_new?fn=_initEntity&target=/.m/taskName&json=\"task2\"&target=/.m/entityId&json=<!/../.m/id!>"),
		@Config(url = "/currentTaskName/_update?rawPayload=\"task2\"")
	})
	private String action_createTempTask2;
	
	@Config(url = "/_process?fn=_setByRule&rule=taskcompletion") 
	private String action_exitCondition_task1;

	@Config(url = "/_process?fn=_setByRule&rule=task2completion") 
	private String action_exitCondition_task2;
	
	@Page
	private VPSampleViewWithBpmnPage page_landing;

}
