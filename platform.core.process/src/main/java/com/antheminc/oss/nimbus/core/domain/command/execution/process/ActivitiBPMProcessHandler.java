/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution.process;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.antheminc.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.antheminc.oss.nimbus.core.domain.definition.Constants;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.platform.spec.model.process.ProcessEngineContext;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
public class ActivitiBPMProcessHandler<T,R> implements FunctionHandler<T,R> {
	
	@Autowired RuntimeService runtimeService;
	private String processId;
	
	@Override
	public R execute(ExecutionContext executionContext, Param<T> actionParameter) {
		ProcessEngineContext context = new ProcessEngineContext(executionContext.getRootModel().getAssociatedParam());
		Map<String, Object> executionVariables = new HashMap<String, Object>();
		executionVariables.put(Constants.KEY_EXECUTE_PROCESS_CTX.code, context);
		runtimeService.startProcessInstanceByKey(processId, executionVariables);		
		
		@SuppressWarnings("unchecked")
		R output = (R) context.getOutput();
		return output;
	}
	
	
	
}
