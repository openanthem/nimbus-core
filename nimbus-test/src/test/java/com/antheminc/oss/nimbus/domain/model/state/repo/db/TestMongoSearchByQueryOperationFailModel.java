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

package com.antheminc.oss.nimbus.domain.model.state.repo.db;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author AG34346
 *
 */
@Domain(value = "testmongosearchbyqueryoperationfailmodel", includeListeners = { ListenerType.persistence,
		ListenerType.update })
@Repo(value = Database.rep_mongodb, cache = Cache.rep_device)
@Getter
@Setter
@ToString(callSuper = true)
public class TestMongoSearchByQueryOperationFailModel extends IdLong {
	private static final long serialVersionUID = 1L;

	@Config(url = "<!#this!>/../result1/_process?fn=_set&url=/p/covid19/_search?fn=query&where={aggregate:\"covid19\",pipeline:[\r\n"
			+ "{ $match: {\"continent\": \"Asia\"}}\r\n"
			+ "]}&collation:{\"locale\":\"en\",\"strength\":2}")
	private String action1;
	private List<CovidData> result1;

	@Config(url = "<!#this!>/../result2/_process?fn=_set&url=/p/covid19/_search?fn=query&where={aggregate:\"covid19\",pipeline:[\r\n"
			+ "{ $match: {\"continent\": \"Asia\"}},\r\n" + "{$sort:{\"country\" : -1} }\r\n"
			+ "]}&collation:{\"locale\":\"en\",\"strength\":2}")
	private String action2;
	private List<CovidData> result2;

}
