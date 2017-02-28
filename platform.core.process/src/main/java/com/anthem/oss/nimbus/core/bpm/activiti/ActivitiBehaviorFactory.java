/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm.activiti;

import java.util.List;

import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.IOParameter;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.behavior.CallActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * Behavior factory class to inject framework overrides to default Activiti Behavior for different tasks.
 * Currently the framework provides a custom implementation for User tasks and Call Activity
 * @author Jayant Chaudhuri
 *
 *
 */
public class ActivitiBehaviorFactory extends DefaultActivityBehaviorFactory {
	
	private String[] frameworkParameters = new String[]{"processGatewayContext","pxhelp"};
	public static final String TARGET_PARAMETER_PATH = "fx_TargetParameterPath";

	@Override
	public UserTaskActivityBehavior createUserTaskActivityBehavior(UserTask userTask) {
		ActivitiUserTaskActivityBehavior platformUserTaskBehavior = new ActivitiUserTaskActivityBehavior(userTask);
		return platformUserTaskBehavior;
	}
	
	@Override
	public CallActivityBehavior createCallActivityBehavior(CallActivity callActivity) {
		
		//The framework adds few custom variables that needs to be passed in all sub processes. 
		//The code below adds those standard in and out parameters
		List<IOParameter> inParameters = callActivity.getInParameters();
		List<IOParameter> outParameters = callActivity.getOutParameters();
		for(String frameworkParameter: frameworkParameters){
			IOParameter param = createIOParameter(frameworkParameter, frameworkParameter, null, null);
			inParameters.add(param);
			outParameters.add(param);
		}
		
	    String expressionRegex = "\\$+\\{+.+\\}";
	    ActivitiCallActivityBehavior callActivityBehaviour = null;
	    if (StringUtils.isNotEmpty(callActivity.getCalledElement()) && callActivity.getCalledElement().matches(expressionRegex)) {
	      callActivityBehaviour = new ActivitiCallActivityBehavior(expressionManager.createExpression(callActivity.getCalledElement()), callActivity.getMapExceptions());
	    } else {
	      callActivityBehaviour = new ActivitiCallActivityBehavior(callActivity.getCalledElement(), callActivity.getMapExceptions()); 
	    }
	    
	    for(IOParameter outputParam: outParameters){
	    	if(outputParam.getSource() != null && outputParam.getSource().equals(TARGET_PARAMETER_PATH)){
	    		callActivityBehaviour.setMappedParameterPath(outputParam.getTarget());
	    	}
	    }
	    return callActivityBehaviour;			
	}
	
	/**
	 * Creates a IO Paramter to facilitate source <-> target process conversions
	 * @param source
	 * @param target
	 * @param sourceExpression
	 * @param targetExpression
	 */
	private IOParameter createIOParameter(String source, String target, String sourceExpression, String targetExpression){
		IOParameter parameter = new IOParameter();
		parameter.setSource(source);
		parameter.setTarget(target);
		parameter.setSourceExpression(sourceExpression);
		parameter.setTargetExpression(targetExpression);
		return parameter;
	}
}
