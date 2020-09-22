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
package com.antheminc.oss.nimbus.test.scenarios.s15;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.LazyLoad;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Tony Lopez
 *
 */
@Domain(value="lazyloadcore", includeListeners = { ListenerType.persistence })
@Repo(alias = "lazyloadcore", value = Database.rep_mongodb, cache = Cache.rep_device)
@Getter @Setter @ToString
public class LazyLoadCore extends IdLong {
	
	private static final long serialVersionUID = 1L;

	@Config(url = "/p/lazyloadcore:<!/id!>/nm/_process?fn=_instantiate&model=/p/lazyloadcore:<!/id!>/nm&param=nm2")
	private String parameter1;
	
	private NestedModel nm;
	
	@Model
	@Getter @Setter
	public static class NestedModel{
		@LazyLoad
		private NestedModel2 nm2;
	}
	
	@Model
	@Getter @Setter
	public static class NestedModel2{
		private String nestedParameter2;
	}
}
