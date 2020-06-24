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
package com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.SampleJPARootCoreEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Tony Lopez
 *
 */
@Domain(value = "sample_jpa_view", includeListeners = { ListenerType.websocket })
@MapsTo.Type(SampleJPARootCoreEntity.class)
@Repo(Database.rep_none)
@Getter
@Setter
public class VRSampleJPARootViewEntity {

	@Grid
	@Config(url = "/p/person/_search?fn=query&where=<!filterCriteria!>")
	@Path(linked = false)
	private List<PersonLineItem> people;
}
