/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.cache.session.PlatformSession;
import com.anthem.nimbus.platform.spec.model.dsl.binder.AssignmentTask;
import com.anthem.nimbus.platform.spec.model.dsl.binder.AssignmentTask.TaskStatus;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadModel;

/**
 * @author Jayant Chaudhuri
 *
 */
@Component
public class TaskCompletionListener implements ExecutionListener {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	TaskService taskService;

	/* (non-Javadoc)
	 * @see org.activiti.engine.delegate.ExecutionListener#notify(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Override
	public void notify(DelegateExecution execution) {
		ActivitiContext aCtx = (ActivitiContext) execution.getVariable(ActivitiProcessGateway.PROCESS_ENGINE_GTWY_KEY);
		QuadModel<?,?> quadModel = PlatformSession.getOrThrowEx(aCtx.getProcessEngineContext().getCommandMsg().getCommand());
		AssignmentTask assignmentTask = (AssignmentTask)quadModel.getCore().getState();
		String taskId = assignmentTask.getTaskId();
		taskService.complete(taskId);
		assignmentTask.setInternalStatus(TaskStatus.COMPLETED);
	}

}
