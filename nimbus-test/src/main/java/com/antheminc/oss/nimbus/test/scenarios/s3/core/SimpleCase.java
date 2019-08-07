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
package com.antheminc.oss.nimbus.test.scenarios.s3.core;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity;
import com.antheminc.oss.nimbus.entity.DateRange;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="core_simplecase", includeListeners={ListenerType.persistence})
@Model(value="core_simplecase", excludeListeners={ListenerType.websocket})
@Repo(value=Database.rep_mongodb, cache=Cache.rep_device)
@Getter @Setter @ToString
public class SimpleCase extends AbstractEntity.IdLong {

	private static final long serialVersionUID = 1L;
    
	private String requestType;

	private String caseType;
	
	private DateRange serviceDate;
	
	private Member patient;
	
	private String[] types;
	
	private ServiceLine oneServiceLine;
	
	private ServiceLine oneServiceLineConverted;
	
	private List<ServiceLine> serviceLines;
	
	private List<ServiceLine> serviceLinesConverted;
}
