/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.anthem.nimbus.platform.spec.model.process.ProcessEngineContext;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessGateway;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.integration.sa.ProcessExecutionCtxHelper;
import com.anthem.oss.nimbus.core.integration.sa.ServiceActivatorException;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
public class ActivitiGateway implements ApplicationContextAware {

	public static final String PROCESS_ENGINE_GTWY_KEY = "processGatewayContext";
	public static final String PROCESS_ENGINE_GTWY_HELPER = "pxhelp";

	RuntimeService runtimeService;
	
	ProcessExecutionCtxHelper processExecutionCtx;

	ApplicationContext applicationContext;

	TaskService taskService;

	ActivitiDAO activitiDAO;
	
	@Autowired
	CommandMessageConverter converter;	
	
	@Qualifier("default.processGateway")
	private ProcessGateway processGateway;	

	public ActivitiGateway(RuntimeService runtimeService, ProcessExecutionCtxHelper processExecutionCtx,
			TaskService taskService, ActivitiDAO activitiDAO,
			ProcessGateway processGateway) {
		
		this.runtimeService = runtimeService;
		this.processExecutionCtx = processExecutionCtx;
		this.taskService = taskService;
		this.activitiDAO = activitiDAO;
		this.processGateway = processGateway;
	}
	
	/**
	 * 
	 * @param cmdMsg
	 * @param processKey
	 * @return
	 */
	public ActivitiResponse executeProcess(CommandMessage cmdMsg, String processKey) {
		ActivitiContext aCtx = prepareContext(cmdMsg);
		Map<String, Object> executionVariables = new HashMap<String, Object>();
		executionVariables.put(PROCESS_ENGINE_GTWY_KEY, aCtx);
		executionVariables.put(PROCESS_ENGINE_GTWY_HELPER, aCtx.getProcessGatewayHelper());
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(processKey, executionVariables);
		if(aCtx.getProcessEngineContext().getOutput() != null) {
			if(aCtx.getProcessEngineContext().getOutput() instanceof Exception) {
				if(aCtx.getProcessEngineContext().getOutput() instanceof ServiceActivatorException) {
					ServiceActivatorException ex = (ServiceActivatorException) aCtx.getProcessEngineContext().getOutput();
					throw ex;
				}
				else {
					Exception ex = (Exception) aCtx.getProcessEngineContext().getOutput();
					throw new ServiceActivatorException(ex);
				}
			}
		}
		Object response =  aCtx.getProcessEngineContext().getOutput(); 
		ActivitiResponse processResponse = new ActivitiResponse();
		processResponse.setExecutionId(pi.getId());
		processResponse.setResponse(response);
		processResponse.setProcessInstance(pi);
		return processResponse;
	}
	
	/**
	 * 
	 * @param cmdMsg
	 * @return
	 */
	public ActivitiContext prepareContext(CommandMessage cmdMsg) {
		ProcessEngineContext pCtx = new ProcessEngineContext();
		pCtx.setCommandMsg(cmdMsg);
		ActivitiContext aCtx = new ActivitiContext();
		aCtx.setProcessEngineContext(pCtx);
		processExecutionCtx.setACtx(aCtx);
		ActivitiGatewayHelper helper = applicationContext.getBean(ActivitiGatewayHelper.class);
		helper.setActivitiContext(aCtx);
		aCtx.setProcessGatewayHelper(helper);
		return aCtx;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
	/**
	 * 
	 * @param cmdMsg
	 * @param quadModel
	 * @return
	 */
	public Object initiateProcessExecution(CommandMessage cmdMsg, QuadModel<?,?> quadModel){
		ActivitiResponse response = executeProcess(cmdMsg, cmdMsg.getCommand().getRootDomainAlias());
		if(response == null)
			return null; 
		return response.getResponse();
	}	

	/**
	 * 
	 * @param cmdMessage
	 * @param quadModel
	 * @param processExecutionId
	 * @return
	 */
	public Object continueProcessExecution(CommandMessage cmdMessage,QuadModel<?,?> quadModel,String processExecutionId){
		if(processExecutionId != null){
			List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processInstanceId(processExecutionId).list();
			if(processInstances != null && processInstances.size() > 0){
				ProcessInstance processInstance = processInstances.get(0);
				if(!processInstance.isEnded()){
					Object resp = continueProcessExecution(cmdMessage,quadModel,processInstance);
					return resp;
				}
			}
		}
		
		Object resp = initiateProcessExecution(cmdMessage,quadModel);
		return resp;		
	}
	
	/**
	 * 
	 * @param cmdMessage
	 * @param quadModel
	 * @param processInstance
	 * @return
	 */
	private Object continueProcessExecution(CommandMessage cmdMessage,QuadModel<?,?> quadModel,ProcessInstance processInstance){
		Task pageTask = updateContextAndFindPageTask(cmdMessage,processInstance.getId());
		if(pageTask != null){
			String taskId = pageTask.getId();
			String taskPageName = pageTask.getTaskDefinitionKey();
			String passedPageId = getSourcePageIdFromCommand(cmdMessage);
			if(passedPageId != null && !passedPageId.equals(taskPageName)){
				activitiDAO.updatePageTaskForExecution(pageTask.getId(), pageTask.getExecutionId(), pageTask.getTaskDefinitionKey(), passedPageId, pageTask.getName());
			}
			Map<String, Object> executionVariables = new HashMap<String, Object>();
			ActivitiContext actContext = prepareContext(cmdMessage);
			executionVariables.put(ActivitiGateway.PROCESS_ENGINE_GTWY_KEY, actContext);
			executionVariables.put(ActivitiGateway.PROCESS_ENGINE_GTWY_HELPER, actContext.getProcessGatewayHelper());
			taskService.complete(taskId,executionVariables);
			return actContext.getProcessEngineContext().getOutput();
		}
		return null;
	}	
	
	/**
	 * 
	 * @param processInstance
	 * @return
	 */
	private Task updateContextAndFindPageTask(CommandMessage cmdMessage,String processInstanceId){
		List<Task> allTasks = new ArrayList<Task>();
		updateContextAndAddTasksForProcess(allTasks,cmdMessage,processInstanceId);
		return findPageTask(allTasks);
	}
	
	/**
	 * 
	 * @param allTasks
	 * @param cmdMessage
	 * @param processInstanceId
	 */
	private void updateContextAndAddTasksForProcess(List<Task> allTasks,CommandMessage cmdMessage,String processInstanceId){
		List<Task> remainingTasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		if(remainingTasks != null && remainingTasks.size() > 0){
			allTasks.addAll(remainingTasks);
		}
		List<ProcessInstance> subprocessList = runtimeService.createProcessInstanceQuery().superProcessInstanceId(processInstanceId).list();
		if(subprocessList == null || subprocessList.size() == 0)
			return;
		for(ProcessInstance pi: subprocessList){
			updateContextAndAddTasksForProcess(allTasks,cmdMessage,pi.getId());
		}
	}
	

	/**
	 * Retrieve passed page id from the command message. If available, then the system will move the BPM back to the page id being passed
	 * @param cmdMessage
	 * @return
	 */
	private String getSourcePageIdFromCommand(CommandMessage cmdMessage){
		String payload = cmdMessage.getRawPayload();
		if(payload == null)
			return null;
		if(!payload.contains("'cpage'"))
			return null;
		CurrentPage cpage = converter.convert(CurrentPage.class, cmdMessage);
		if(cpage != null && cpage.getCpage() != null){
			return cpage.getCpage();
		}
		return null;
	}
	
	/**
	 * 
	 * @param tasks
	 * @return
	 */
	private Task findPageTask(List<Task> tasks){
		if(tasks == null)
			return null;
		for(Task task: tasks){
			return task;
		}		
		return null;
	}	
	
	@Getter @Setter
	public class CurrentPage{
		private String cpage;
	}
}
