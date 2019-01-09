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

package com.antheminc.oss.nimbus.test.scenarios.repo.remote.view;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Executions.Configs;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.test.scenarios.repo.remote.core.SampleRemoteRepo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Swetha Vemuri
 *
 */
@Domain(value="vr_remote_repo", includeListeners={ListenerType.update})
@Repo(value=Database.rep_none, cache=Cache.rep_device)
@MapsTo.Type(SampleRemoteRepo.class)
@Getter @Setter @ToString(callSuper=true)
public class VRSampleRemoteRepo {
	
	private String vr_attr1;
	
	
	@Configs({
		@Config(url="/.d/.m/attr1/_update?rawPayload=\"fromlocalToRemote\""),
		@Config(url="/p/vr_remote_repo2/_new?fn=_initEntity&target=/.m/attr1&json=\"vr_remote_repo2_attr1_init\""),
		@Config(url="/p/vr_remote_repo2:1/_delete")
		
	})
	private String vr_attr2;
	
	
}
