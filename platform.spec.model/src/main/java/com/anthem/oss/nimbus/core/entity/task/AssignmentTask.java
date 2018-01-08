/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.task;

import java.time.LocalDate;
import java.util.Arrays;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;

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
	
	private TaskSource source;

	@NotNull
	private String taskType;
	private String taskTypeForDisplay;
	
	private String taskName;
	
	private String description;

	private LocalDate startDate;

	private LocalDate dueDate;

	private TaskPriority priority;
	
	private LocalDate appointmentDate;

	private String status;
	
	private String entityId;
	
	private String parentId; // e.g. OrientationTask
	
	private String queueCode;
	
	private String recurrence;
	
	private String reminder;
		
	private String testTaskType;
	
	private String taskStatus; // TODO - temp attr - to be removed (Rakesh)
	
	public enum TaskPriority{
		Urgent,	
		High,
		Medium,
		Low
	}
	
	public enum TaskSource {
		manual,
		systematic
	}
	
	// TODO - refactor and review - Rakesh
	public String getTaskTypeForDisplay() {
		return taskType;
	}
	
}
