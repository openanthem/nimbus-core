package com.anthem.oss.nimbus.core.bpm.activiti;

import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.ExtensionElement;
import org.activiti.bpmn.model.UserTask;
import org.apache.commons.collections.CollectionUtils;

abstract public class AbstractUserTaskExtension implements UserTaskExtension{

	public String getExtensionValue(UserTask userTask,String extensionName){
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
