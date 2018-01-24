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
package com.anthem.oss.nimbus.test.sample.um.model.core;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.AbstractEntity;
import com.antheminc.oss.nimbus.entity.DateRange;
import com.anthem.oss.nimbus.core.domain.definition.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Domain(value="core_umcase", includeListeners={ListenerType.persistence})
@Model(value="core_umcase", excludeListeners={ListenerType.websocket})
@Repo(value=Database.rep_mongodb, cache=Cache.rep_device)
@ToString
public class UMCase extends AbstractEntity.IdString {

	private static final long serialVersionUID = 1L;
    
	@Getter @Setter private String requestType;

	@Getter @Setter private String caseType;
	
	@Getter @Setter private DateRange serviceDate;
	
	@Getter @Setter private Patient patient;
	
	

	@Getter @Setter private String[] types;
	
	@Getter @Setter private ServiceLine oneServiceLine;
	
	@Getter @Setter private ServiceLine oneServiceLineConverted;
	
	@Getter @Setter private List<ServiceLine> serviceLines;
	
	@Getter @Setter private List<ServiceLine> serviceLinesConverted;
}
