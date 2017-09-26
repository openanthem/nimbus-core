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
	private TaskType taskType;
	private String taskTypeForDisplay;
	
	private String taskName;
	
	private String description;

	private LocalDate startDate;

	private LocalDate dueDate;

	private TaskPriority priority;
	
	private LocalDate appointmentDate;

	private TaskStatus status;
	
	private String entityId;
	
	private String queueCode;
	
	private String recurrence;
	
	private String reminder;
		
	private String testTaskType;
	
	private String taskStatus; // TODO - temp attr - to be removed (Rakesh)
	
	public enum TaskStatus{
		Open,
		Completed,
		Cancelled;
		
		public static TaskStatus findByStatusString(String status) {
			return Arrays.asList(TaskStatus.values()).stream()
					.filter((taskStatus) -> StringUtils.equalsIgnoreCase(status, taskStatus.name()))
					.findFirst()
					.orElse(null);
		}
	}
	
	public enum TaskPriority{
		Urgent,	
		High,
		Medium,
		Low
	}
	
	public enum TaskType {
		patienteligibility,
		initialoutreach,
		patientenrollment;
	}
	
	public enum TaskSource {
		manual,
		systematic
	}
	
	// TODO - refactor and review - Rakesh
	public String getTaskTypeForDisplay() {
		if(this.getTaskType() != null) {
			if(this.getTaskType().name().equalsIgnoreCase("patienteligibility")) {
				return "Patient Eligibility";
			} else if(this.getTaskType().name().equalsIgnoreCase("patientenrollment")) {
				return "Patient Enrollment";
			} else if(this.getTaskType().name().equalsIgnoreCase("initialoutreach")) {
				return "Initial Outreach";
			}
			return this.getTaskType().name();
		}
		return "";
	}
	
}
