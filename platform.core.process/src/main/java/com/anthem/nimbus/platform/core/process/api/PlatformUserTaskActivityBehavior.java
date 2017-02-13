/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import java.rmi.server.UID;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.anthem.nimbus.platform.core.process.api.exec.TaskInitializer;
import com.anthem.nimbus.platform.core.process.api.exec.TaskRouter;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param;
import com.anthem.nimbus.platform.spec.model.view.dsl.config.ViewParamConfig;
import com.anthem.nimbus.platform.utils.converter.NavigationStateHelper;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessGateway;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;
import com.anthem.oss.nimbus.core.entity.process.PageHolder;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

import lombok.Getter;
import lombok.Setter;


/**
 * @author Jayant Chaudhuri
 *
 */

@Getter @Setter
public class PlatformUserTaskActivityBehavior extends UserTaskActivityBehavior {

	private static final long serialVersionUID = 1L;
	public static final String PLATFORM_USER_TASK_TYPE= "userTaskType";
	public static final String PLATFORM_USER_TASK_PAGE= "page";
	public static final String PLATFORM_USER_TASK_ASSIGNMENT= "assignment";
    //@Autowired

	

	public PlatformUserTaskActivityBehavior(UserTask userTask) {
		super(userTask);
	} 
	
	@Override
	public void trigger(DelegateExecution execution, String signalName, Object signalData) {
		super.trigger(execution, signalName, signalData);
	}
	
	@Override
	public void execute(DelegateExecution execution) {
		super.execute(execution);
		String userTaskType = getExtensionValue(PLATFORM_USER_TASK_TYPE);
		if(userTaskType != null && userTaskType.equals(PLATFORM_USER_TASK_PAGE)){
			createPageTask(execution);
		}else if(userTaskType != null && userTaskType.equals(PLATFORM_USER_TASK_ASSIGNMENT)){
			createAssignmentTask(execution);
		}
	}
	
	/**
	 * 
	 * @param execution
	 */
	private void createPageTask(DelegateExecution execution) {
        ActivitiContext aCtx = (ActivitiContext) execution.getVariable(ActivitiProcessGateway.PROCESS_ENGINE_GTWY_KEY);
        QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(aCtx.getProcessEngineContext().getCommandMsg().getCommand());
        Param<?> param = quadModel.getFlow().findParamByPath("/navigationState/pageHolder");
        PageHolder pageHolder = (PageHolder)param.getState();
        UID uid = new UID();
        pageHolder.addPage(uid.toString(), userTask.getId(), userTask.getName());
        String pageId = userTask.getId();
        ViewParamConfig<?> currentPage = (ViewParamConfig<?>) quadModel.getView().getConfig().findParamByPath("/" + pageId);
        NavigationStateHelper navigationStateHelper = ProcessBeanResolver.appContext.getBean(NavigationStateHelper.class);
        navigationStateHelper.activateBreadCrumbForPage(quadModel, currentPage);
        quadModel.getFlow().findStateByPath("/navigationState/pageHolder").setState(pageHolder);
    }




    /**
	 * 
	 * @param execution
	 */
	private void createAssignmentTask(DelegateExecution execution){
		String assignmentTaskType = getExtensionValue("assignmentTaskType");
		String assignmentTaskAssigner = getExtensionValue("assignmentTaskAssigner");
		String assignmentTaskInitializer = getExtensionValue("assignmentTaskInitializer");
		String assignmentTaskAssignerType = getExtensionValue("assignmentTaskAssignerType");
		String assignmentTaskInitializerType = getExtensionValue("assignmentTaskInitializerType");
		ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
		ExpressionManager expressionManager = processEngineConfiguration.getExpressionManager();
		
		/* Create assignment task */
		ActivitiContext aCtx = (ActivitiContext) execution.getVariable(ActivitiProcessGateway.PROCESS_ENGINE_GTWY_KEY);
		QuadModel<?,?> quadModel = UserEndpointSession.getOrThrowEx(aCtx.getProcessEngineContext().getCommandMsg().getCommand());
		LinkedList<Behavior> behavior = new LinkedList<Behavior>();
		behavior.add(Behavior.$execute);
		Command cmd = aCtx.getProcessEngineContext().getCommandMsg().getCommand().createNewCommandForCurrentUser(assignmentTaskType,Action._new,behavior);
		
		//Command c = CommandBuilder.withUri("platform/admin/p/patientenrollment/_new").getCommand();
		//c.setAction(Action._new);
		//c.templateBehaviors().add(Behavior.$execute);
		
		ProcessGateway processGateway = (ProcessGateway)ProcessBeanResolver.appContext.getBean("default.processGateway");
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		//QuadModel<?, ?> q = retrieveResponse(processGateway.startProcess(cmdMsg));
		//AssignmentTask assignmentTask = (AssignmentTask)q.getCore().getState();
		processGateway.startProcess(cmdMsg);
		QuadModel<?, ?> q = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		
		Object coreModel = quadModel.getCore().getState();
		q.getCore().findStateByPath("/taskId").setState(findMatchingTaskInstanceId(execution));
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
	
	/**
	 * 
	 * @param execution
	 * @return
	 */
	private String findMatchingTaskInstanceId(DelegateExecution execution){
		ExecutionEntity executionEntity = (ExecutionEntity)execution;
		List<TaskEntity> tasks = executionEntity.getTasks();
		for(TaskEntity task: tasks){
			if(task.getTaskDefinitionKey().equals(userTask.getId())){
				return task.getId();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param extensionName
	 * @return
	 */
	private String getExtensionValue(String extensionName){
		Map<String, List<ExtensionElement>> extensionElements = userTask.getExtensionElements();
		if(extensionElements == null)
			return null;
		List<ExtensionElement> extensionElementList = extensionElements.get(extensionName);
		if(CollectionUtils.isEmpty(extensionElementList))
			return null;
		ExtensionElement extensionElement = extensionElementList.get(0);
		return extensionElement.getElementText();
		
	}

}