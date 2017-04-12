/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm.activiti;

import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.apache.commons.collections.CollectionUtils;

import com.anthem.oss.nimbus.core.utils.ProcessBeanResolver;

import lombok.Getter;
import lombok.Setter;


/**
 * This class provides framework extension for human tasks.
 * The framework introduces two types of humand task in addition to the existing human task
 * 
 * <ul>
 * <li> Page task : {@link PageTaskExtension}
 * <li> Assignment task : {@link AssignmentTaskExtension}
 * </ul>
 * 
 * @author Jayant Chaudhuri
 *
 */

@Getter @Setter
public class ActivitiUserTaskActivityBehavior extends UserTaskActivityBehavior {

	private static final long serialVersionUID = 1L;
	public static final String PLATFORM_USER_TASK_TYPE= "userTaskType";
	public static final String PLATFORM_USER_TASK_PAGE= "page";
	public static final String PLATFORM_USER_TASK_ASSIGNMENT= "assignment";

	public ActivitiUserTaskActivityBehavior(UserTask userTask) {
		super(userTask);
	} 
	
	@Override
	public void trigger(DelegateExecution execution, String signalName, Object signalData) {
		super.trigger(execution, signalName, signalData);
	}
	
	@Override
	public void execute(DelegateExecution execution) {
		super.execute(execution);
		UserTaskExtension extension = findUserTaskExtension();
		if(extension != null){
			extension.execute(userTask, execution);
		}
	}
	
	private UserTaskExtension findUserTaskExtension(){
		String userTaskType = getExtensionValue(PLATFORM_USER_TASK_TYPE);
		if(userTaskType != null && userTaskType.equals(PLATFORM_USER_TASK_PAGE)){
			return ProcessBeanResolver.getBean(PageTaskExtension.class);
		}else if(userTaskType != null && userTaskType.equals(PLATFORM_USER_TASK_ASSIGNMENT)){
			return ProcessBeanResolver.getBean(AssignmentTaskExtension.class);
		}		
		return null;
	}
	
	public String getExtensionValue(String extensionName){
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