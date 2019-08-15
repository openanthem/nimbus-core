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
/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.s14.view;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Type;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.event.EventType;
import com.antheminc.oss.nimbus.domain.defn.extension.ConfigConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.Script;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;
import com.antheminc.oss.nimbus.test.scenarios.s14.core.StChangeOnStLoadCore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain(value="stchangeonstloadview")
@Repo(Database.rep_none)
@Getter @Setter
@Type(StChangeOnStLoadCore.class)
public class StChangeOnStLoadModel extends IdLong{
	private static final long serialVersionUID = 1L;
	
	private SubModel inline_para;
	

	@Model
	@Getter @Setter
	public static class SubModel{
		@Script(type=Script.Type.SPEL_INLINE, value="findParamByPath('/../parameter3').setState('Value3')", eventType=EventType.OnStateLoad)
		private String parameter2;
		
		@ConfigConditional(
				config= {@Config(url="/.d/inline_para/parameter4/_replace?rawPayload={\"name\":\"Test\"}")}
		)
		private String parameter3;
		
		private SubModel2 parameter4;
	}
	
	@Model
	@Getter @Setter
	public static class SubModel2{
		private String name;
	}
	
}
