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
package com.anthem.oss.nimbus.core.entity.task;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Cache;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain(value="assignmenttask", includeListeners={ListenerType.persistence})
@Repo(alias="assignmenttask",value=Database.rep_mongodb, cache=Cache.rep_device)
@Getter @Setter
public class AssignmentTask extends AbstractEntity.IdString{
	
	private static final long serialVersionUID = 1L;
	
	private String source;

	@NotNull
	private String taskType;
	
	private String taskName;
	
	private String description;

	private LocalDate startDate;

	private LocalDate dueDate;

	private String priority;
	
	private LocalDate appointmentDate;

	private String status;
	
	private String entityId;
	
	private String parentId;
	
	private String queueCode;
	
	private String recurrence;
	
	private String reminder;
		
}
