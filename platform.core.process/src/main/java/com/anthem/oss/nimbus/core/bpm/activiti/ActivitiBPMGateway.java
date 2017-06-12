/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm.activiti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import com.anthem.nimbus.platform.spec.model.process.ProcessEngineContext;
import com.anthem.oss.nimbus.core.bpm.BPMGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.definition.Constants;

/**
 * @author Jayant Chaudhuri
 *
 */
public class ActivitiBPMGateway implements BPMGateway {
	
	@Autowired RuntimeService runtimeService;
	
	@Autowired TaskService taskService;

	@Override
	public String startBusinessProcess(ExecutionContext eCtx, String processId) {
		ProcessEngineContext context = new ProcessEngineContext(eCtx, null);
		Map<String, Object> executionVariables = new HashMap<String, Object>();
		executionVariables.put(Constants.KEY_EXECUTE_PROCESS_CTX.code, context);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(processId, executionVariables);			
		return pi.getId();
	}


	@Override
	public Object continueBusinessProcessExecution(ExecutionContext eCtx, String processExecutionId) {
		List<Task> pendingTasks = taskService.createTaskQuery().processInstanceId(processExecutionId).list();
		ProcessEngineContext context = new ProcessEngineContext(eCtx, null);
		Map<String, Object> executionVariables = new HashMap<String, Object>();
		executionVariables.put(Constants.KEY_EXECUTE_PROCESS_CTX.code, context);		
		for(Task task: pendingTasks){
			taskService.complete(task.getId(),executionVariables);
		}
		return context.getOutput();
	}

}
