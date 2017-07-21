/**
 * 
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
	
	private String taskName;
	
	private TaskStatus status;
	
	private String description;
	
	@NotNull
	private TaskType taskType;
	
	private String testTaskType;
	
	private LocalDate dueDate;
	
	private LocalDate startDate;
	
	private TaskPriority priority;
	
	private String entityId;
	
	private String queueCode;
	
	private String recurrence;
	
	private String reminder;
	
	private Source source;
	
	private String taskTypeForDisplay;
	
	public enum TaskStatus{
		Open,
		Completed,
		Cancelled,
	}
	
	public enum TaskPriority{
		Urgent,	
		High,
		Medium,
		Low
	}
	
	public enum TaskType {
		patienteligibility,
		patientenrollment;
	}
	
	public enum Source {
		manual,
		systematic
	}
	
	
	// TODO - refactor and review - Rakesh
	public String getTaskTypeForDisplay() {
		if(this.getTaskType() != null) {
			if(this.getTaskType().name().equalsIgnoreCase("patienteligibility")) {
				return "Patient Eligibility";
			}
			else if(this.getTaskType().name().equalsIgnoreCase("patientenrollment")) {
				return "Patient Enrollment";
			}
			return this.getTaskType().name();
		}
		return "";
	}
	
}
