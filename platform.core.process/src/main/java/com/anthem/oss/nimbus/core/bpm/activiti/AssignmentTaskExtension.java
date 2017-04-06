package com.anthem.oss.nimbus.core.bpm.activiti;

import java.util.LinkedList;
import java.util.List;

import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.bpm.TaskInitializer;
import com.anthem.oss.nimbus.core.bpm.TaskRouter;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessGateway;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.core.utils.ProcessBeanResolver;


public class AssignmentTaskExtension extends AbstractUserTaskExtension {

	@Override
	public void execute(UserTask userTask, DelegateExecution execution) {
		String assignmentTaskType = getExtensionValue(userTask,"assignmentTaskType");
		String assignmentTaskAssigner = getExtensionValue(userTask,"assignmentTaskAssigner");
		String assignmentTaskInitializer = getExtensionValue(userTask,"assignmentTaskInitializer");
		String assignmentTaskAssignerType = getExtensionValue(userTask,"assignmentTaskAssignerType");
		String assignmentTaskInitializerType = getExtensionValue(userTask,"assignmentTaskInitializerType");
		ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
		ExpressionManager expressionManager = processEngineConfiguration.getExpressionManager();
		
		/* Create assignment task */
		ActivitiContext aCtx = (ActivitiContext) execution.getVariable(ActivitiGateway.PROCESS_ENGINE_GTWY_KEY);
		QuadModel<?,?> quadModel = UserEndpointSession.getOrThrowEx(aCtx.getProcessEngineContext().getCommandMsg().getCommand());
		LinkedList<Behavior> behavior = new LinkedList<Behavior>();
		behavior.add(Behavior.$execute);
		Command cmd = aCtx.getProcessEngineContext().getCommandMsg().getCommand().createNewCommandForCurrentUser(assignmentTaskType,Action._new,behavior);
		
		
		ProcessGateway processGateway = (ProcessGateway)ProcessBeanResolver.appContext.getBean("default.processGateway");
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		processGateway.startProcess(cmdMsg);
		QuadModel<?, ?> q = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		
		Object coreModel = quadModel.getCore().getState();
		q.getCore().findStateByPath("/taskId").setState(findMatchingTaskInstanceId(userTask,execution));
		q.getCore().findModelByPath("/entity").setState(coreModel);
		AbstractEntity.IdString model = (AbstractEntity.IdString)q.getCore().getState();
		cmd.getElement(Type.DomainAlias).get().setRefId(model.getId());
		UserEndpointSession.setAttribute(cmd, q);
		execution.setVariable("assignmentTask", q.getCore().getState());
		
		/* Initialize assignment task */
		Expression expression = expressionManager.createExpression(assignmentTaskInitializerType);	
		String initiliazerType = (String)expression.getValue(execution);
		
		if(StringUtils.isNotBlank(initiliazerType)) {
			TaskInitializer taskInitializer = ProcessBeanResolver.getBean(initiliazerType, TaskInitializer.class);
			taskInitializer.initialize(q, assignmentTaskInitializer);
		}
		
		/* Assign assignment task to a user/ queue */
		expression = expressionManager.createExpression(assignmentTaskAssignerType);	
		String assignerType = (String)expression.getValue(execution);
		
		if(StringUtils.isBlank(assignerType)) return; 
			
		TaskRouter taskRouter = ProcessBeanResolver.getBean(assignerType, TaskRouter.class);
		taskRouter.route(q, assignmentTaskAssigner);

	}
	
	private String findMatchingTaskInstanceId(UserTask userTask,DelegateExecution execution){
		ExecutionEntity executionEntity = (ExecutionEntity)execution;
		List<TaskEntity> tasks = executionEntity.getTasks();
		for(TaskEntity task: tasks){
			if(task.getTaskDefinitionKey().equals(userTask.getId())){
				return task.getId();
			}
		}
		return null;
	}	

}
