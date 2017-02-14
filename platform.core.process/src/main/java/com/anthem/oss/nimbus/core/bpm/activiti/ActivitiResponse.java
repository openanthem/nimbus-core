/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm.activiti;

import java.util.List;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;

import com.anthem.oss.nimbus.core.domain.command.execution.ProcessResponse;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
public class ActivitiResponse extends ProcessResponse {

	private ProcessInstance processInstance;
	
	/**
	 * 
	 * @return
	 */
	public List<TaskEntity> getTasks(){
		return ((ExecutionEntity)processInstance).getTasks();
	}
}
