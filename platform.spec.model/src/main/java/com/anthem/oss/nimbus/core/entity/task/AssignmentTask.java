/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.task;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain(value="assignmenttask", includeListeners={ListenerType.persistence})
@Repo(alias="assignmenttask")
//@AssociatedEntity("flow_cmcase")
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
	
	public enum TaskStatus{
		Open,
		Complete,
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
	
	
}
