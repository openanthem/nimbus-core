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
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessResponse;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
public class ActivitiBPMGateway implements BPMGateway {
	
	@Autowired RuntimeService runtimeService;
	
	
	@Autowired TaskService taskService;
	
	@Override
	public ProcessResponse startBusinessProcess(Param<?> param, String processId) {
		ProcessEngineContext context = new ProcessEngineContext(param);
		Map<String, Object> executionVariables = new HashMap<String, Object>();
		executionVariables.put(Constants.KEY_EXECUTE_PROCESS_CTX.code, context);
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(processId, executionVariables);
		ProcessResponse response = new ProcessResponse();
		response.setResponse(context.getOutput());
		response.setExecutionId(pi.getId());
		return response;
	}
	
	@Override
	public Object continueBusinessProcessExecution(Param<?> param, String processExecutionId) {
		List<Task> pendingTasks = taskService.createTaskQuery().processInstanceId(processExecutionId).list();
		ProcessEngineContext context = new ProcessEngineContext(param);
		Map<String, Object> executionVariables = new HashMap<String, Object>();
		executionVariables.put(Constants.KEY_EXECUTE_PROCESS_CTX.code, context);		
		for(Task task: pendingTasks){
			taskService.complete(task.getId(),executionVariables);
		}
		return context.getOutput();
	}

}
