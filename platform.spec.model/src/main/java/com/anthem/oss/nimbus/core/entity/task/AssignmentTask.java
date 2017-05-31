/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.task;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.anthem.oss.nimbus.core.domain.definition.AssociatedEntity;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
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
@AssociatedEntity("flow_cmcase")
@Getter @Setter
public class AssignmentTask extends AbstractEntity.IdString{
	
	private static final long serialVersionUID = 1L;
	
	private String taskId; //TODO change the name to bpmn id so that id is used from abstractmodel.IdString
	
	private String taskName;
	
	//@Model.Param.Values(url="staticCodeValue-/taskStatus")
	private TaskStatus status;
	
	private String description;
	
	// (e.g. patientEnrollmentTask... so this will help us avoid check for instance of, abstract method)
	@NotNull
	//@Model.Param.Values(url="staticCodeValue-/taskType")
	private TaskType taskType;
	
	private LocalDate dueDate;
	
	private LocalDate startDate;
	
	//@Model.Param.Values(url="staticCodeValue-/taskPriority")
	private TaskPriority priority;
	
	private Object entityId;
	
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
