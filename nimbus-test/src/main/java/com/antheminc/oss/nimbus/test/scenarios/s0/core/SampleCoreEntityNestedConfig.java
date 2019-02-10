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
package com.antheminc.oss.nimbus.test.scenarios.s0.core;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Executions.Configs;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.extension.ConfigConditional;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Rakesh Patel
 *
 */
@Domain(value = "sample_core_nested", includeListeners = { ListenerType.persistence })
@Repo(Database.rep_mongodb)
@Getter
@Setter
public class SampleCoreEntityNestedConfig extends IdLong {

	private static final long serialVersionUID = 1L;

	private String attr_String;
	
	private String testParam;
	
	private String testParam2;
	
	private String testParam3;
	
	@Config(url="/testParam3/_update?rawPayload=\"<!../<!../testParam2!>!>\"")
	private String paramConfigWithNestedPath;
	
	@Config(url="/testParam3/_update?rawPayload=\"<!../testParam!><!../testParam2!>\"")
	private String paramConfigWithNestedPath2;
	
	@ConfigConditional(
		when="true", config = {
			@Config(url="<!#this!>/../testParam/_update?rawPayload=\"testParam\""),
			@Config(url="<!#this!>/_process?fn=_setByRule&rule=demoassociatedparams&associatedParam=<!#this!>/../testParam/_get")
		}
	)
	private String paramWithSetByRuleConfig;
	
}

