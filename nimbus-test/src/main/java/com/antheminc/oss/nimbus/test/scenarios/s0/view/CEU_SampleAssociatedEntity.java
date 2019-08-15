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
package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Executions.Configs;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreAssociatedEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 * Flow: Process Core Entity Update (attr_String)
 * Acronym: CEU
 *
 */
@Domain(value="ceu_sampleassociatedentity")
@Repo(value=Database.rep_none, cache=Cache.rep_device)
@Getter @Setter
public class CEU_SampleAssociatedEntity {

	private Long entityId;
    
	@Path(linked=false)
	private List<SampleCoreAssociatedEntity> allAssociatedEntities;

	@Configs({
		@Config(url="/entityId/_update"),
		@Config(url="/allAssociatedEntities/_process?fn=_set&url=/p/sample_coreassociatedentity/_search?fn=query&where=sample_coreassociatedentity.entityId.eq(<!/entityId!>)"),
		@Config(url="/p/sample_coreassociatedentity:<!col/id!>/status/_update?rawPayload=\"Cancelled\"", col="<!/allAssociatedEntities!>")
	})
    public String action_updateStatus;
   
}