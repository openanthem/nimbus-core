package com.anthem.oss.nimbus.core.bpm.activiti;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;

public interface UserTaskExtension {
	public void execute(UserTask userTask,DelegateExecution execution);
}
