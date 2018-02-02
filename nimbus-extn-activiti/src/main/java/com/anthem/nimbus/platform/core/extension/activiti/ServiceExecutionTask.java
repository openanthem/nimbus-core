package com.anthem.nimbus.platform.core.extension.activiti;

import org.activiti.designer.integration.annotation.Help;
import org.activiti.designer.integration.annotation.Property;
import org.activiti.designer.integration.annotation.PropertyItems;
import org.activiti.designer.integration.annotation.Runtime;
import org.activiti.designer.integration.servicetask.AbstractCustomServiceTask;
import org.activiti.designer.integration.servicetask.PropertyType;


/**
 * @author AC67870
 *
 */

@Runtime(javaDelegateExpression="${serviceExecutionDelegate}")
public class ServiceExecutionTask extends AbstractCustomServiceTask {

	@Override
	public String getName() {
		return "Service Activator Task";
	}
	
	@Override
	public String contributeToPaletteDrawer() {
	    return "Nimbus - Not Used";
	}
	
	@Override
	public String getSmallIconPath() {
	    return "icons/ServiceTask.png";
	}
	
	
	@Property(type=PropertyType.TEXT, displayName="Service name", required=true)
	@Help(displayHelpShort="The service client class that handles the service invocation")
	private String serviceName;
	
	
	@Property(type=PropertyType.TEXT, displayName="Service method name", required=true)
	@Help(displayHelpShort="The service client method that handles the service invocation")
	private String serviceMethod;

	
	@Property(type=PropertyType.TEXT, displayName ="Service execution handler name",defaultValue="defaultExecutionHandler", required=true)
	@Help(displayHelpShort="Service method input payload type",displayHelpLong="The type of the input argument to service method")
	private String executionHandler;
	
	
	@Property(type=PropertyType.RADIO_CHOICE, displayName ="Request handler type",required=true, defaultValue="Default")
	@PropertyItems({"Rule based (Default)","Default", "Script based","Script", "Custom Handler","Custom"})
	@Help(displayHelpShort="Service request handler type",displayHelpLong="Service request handler type")
	private String requestHandlerType;
	
	
	@Property(type=PropertyType.TEXT, displayName ="Request handler name")
	@Help(displayHelpShort="Request handler name",displayHelpLong="Provide custom handler bean name if custom handler selected. "
			+ "If rule based handler selected, by default it will be derived as \"Service name_Service method name_request\""
			+ "If script based handler selected, by default it will be derived as \"Service name_Service method name_request\"")
	private String requestHandlerName;
	
	
	@Property(type=PropertyType.RADIO_CHOICE, displayName ="Response handler type")
	@PropertyItems({"Rule based (Default)","Default", "Script based","Script", "Custom Handler","Custom"})
	@Help(displayHelpShort="Service response handler type",displayHelpLong="Service response handler type")
	private String responseHandlerType;
	
	
	@Property(type=PropertyType.TEXT, displayName="Response handler name")
	@Help(displayHelpShort="Response handler name",displayHelpLong="Response handler name is optional for intermediate service activator tasks; required for final service activator task in the process."
			+ "If rule based handler selected, provide the rule name"
			+ "If script based handler selected, provide the groovy file name")
	private String responseHandlerName;
}
