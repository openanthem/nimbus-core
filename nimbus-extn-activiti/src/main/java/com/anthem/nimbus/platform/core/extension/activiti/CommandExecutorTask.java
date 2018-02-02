/**
 * 
 */
package com.anthem.nimbus.platform.core.extension.activiti;

import org.activiti.designer.integration.annotation.Help;
import org.activiti.designer.integration.annotation.Property;
import org.activiti.designer.integration.annotation.Runtime;
import org.activiti.designer.integration.servicetask.AbstractCustomServiceTask;
import org.activiti.designer.integration.servicetask.PropertyType;

/**
 * @author Jayant Chaudhuri
 *
 */
@Runtime(javaDelegateExpression="${commandExecutorTaskDelegate}")
public class CommandExecutorTask extends AbstractCustomServiceTask {
	
	@Property(type=PropertyType.MULTILINE_TEXT, displayName="URL", required=true)
	@Help(displayHelpShort="Command URL")
	private String url;	

	@Override
	public String getName() {
		return "Command Executor Task";
	}
	
	@Override
	public String contributeToPaletteDrawer() {
	    return "Nimbus - Service Task";
	}
	
	@Override
	public String getSmallIconPath() {
	    return "icons/serviceTask.png";
	}	

}
