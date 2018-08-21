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
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;


@Domain(value="sample_cmd_test", includeListeners={ListenerType.websocket})
@Repo(value = Database.rep_mongodb, cache = Cache.rep_device)
@Getter @Setter
public class SampleCommandTestEntity extends AbstractEntity.IdLong{

	private static final long serialVersionUID = 1L;
	private String test_parameter1;
	private String test_parameter2;
	
	@Configs({
		@Config(url="/test_parameter1/_process?fn=_set&value=<!#env.test.key1!>"),
		@Config(url="/test_parameter2/_process?fn=_set&value=<!#env.test.key2!>")
	})
	private String action_setEnvProperty;
}
