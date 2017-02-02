/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import com.anthem.nimbus.platform.spec.model.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
abstract public class AssignmentTask extends AbstractModel.IdString{
	
	private static final long serialVersionUID = 1L;
	
	private String taskId; //TODO change the name to bpmn id so that id is used from abstractmodel.IdString
	
	private String taskName;

	// (e.g. patientEnrollmentTask... so this will help us avoid check for instance of, abstract method)
	private String taskType;
	
	private LocalDate dueDate;
	
	private String priority;
	private String queueCode;
	private TaskStatus status;

	abstract public void setEntity(Object entity);
	
	public enum TaskStatus{
		IN_PROGRESS,
		COMPLETED,
		OPEN
	}
	
	
	
	
}
