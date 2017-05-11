/**
 * 
 */
package com.anthem.nimbus.platform.core.process.handler;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;

import com.anthem.nimbus.platform.spec.model.process.ProcessEngineContext;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionContext;

/**
 * @author Jayant Chaudhuri
 *
 */
public class ActivitiBPMProcessHandler implements FunctionHandler {
	
	@Autowired RuntimeService runtimeService;
	private String processId;
	
	@Override
	public <R> R executeProcess(ExecutionContext executionContext, Param<?> actionParameter) {
		ProcessEngineContext context = new ProcessEngineContext(executionContext, actionParameter);
		Map<String, Object> executionVariables = new HashMap<String, Object>();
		executionVariables.put(ProcessKeys.processContext.name(), context);
		runtimeService.startProcessInstanceByKey(processId, executionVariables);		
		
		@SuppressWarnings("unchecked")
		R output = (R) context.getOutput();
		return output;		
	}
	
	@Override
	public String getUri() {
		return processId;
	}

	public static enum ProcessKeys{
		processContext
	}
	
}
