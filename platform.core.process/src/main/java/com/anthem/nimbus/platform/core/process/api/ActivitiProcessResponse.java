/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import java.util.List;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;

import com.anthem.nimbus.platform.spec.contract.process.ProcessResponse;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
public class ActivitiProcessResponse extends ProcessResponse {

	private ProcessInstance processInstance;
	
	/**
	 * 
	 * @return
	 */
	public List<TaskEntity> getTasks(){
		return ((ExecutionEntity)processInstance).getTasks();
	}
}
