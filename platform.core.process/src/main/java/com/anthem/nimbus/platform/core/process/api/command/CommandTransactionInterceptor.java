/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.command;

import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.domain.Behavior;
import com.anthem.oss.nimbus.core.domain.execution.ExecuteOutput;
import com.anthem.oss.nimbus.core.domain.execution.MultiExecuteOutput;

/**
 * @author Soham Chakravarti
 *
 */
@Component
public class CommandTransactionInterceptor {

	public MultiExecuteOutput handleResponse(MultiExecuteOutput rawResult) {
		return rawResult;
	}
	
	public MultiExecuteOutput handleResponse(ExecuteOutput.BehaviorExecute<?> rawResult) {
		return new MultiExecuteOutput(rawResult);
	}
	
	public MultiExecuteOutput handleResponse(ExecuteOutput<?> rawResult) {
		ExecuteOutput.BehaviorExecute<?> bExec = new ExecuteOutput.BehaviorExecute<>(Behavior.$execute, rawResult.getResult());
		bExec.setExecuteException(rawResult.getExecuteException());
		bExec.setValidationResult(rawResult.getValidationResult());
		
		return handleResponse(bExec);
	}
	
	public MultiExecuteOutput handleResponse(Holder<?> rawResult) {
		return handleResponse(rawResult.getState());
	}
	
	public MultiExecuteOutput handleResponse(Object rawResult) {
		if(rawResult instanceof MultiExecuteOutput) {
			return handleResponse((MultiExecuteOutput)rawResult);
		}
		
		if(rawResult instanceof ExecuteOutput.BehaviorExecute) {
			return handleResponse((ExecuteOutput.BehaviorExecute<?>)rawResult);
		}
		
		if(rawResult instanceof ExecuteOutput) {
			return handleResponse((ExecuteOutput<?>)rawResult);
		}
		
		if(rawResult instanceof Holder) {
			return handleResponse((Holder<?>)rawResult);
		}
		
		MultiExecuteOutput mExecOutput = new MultiExecuteOutput(new ExecuteOutput.BehaviorExecute<>(Behavior.$execute, rawResult));
		return handleResponse(mExecOutput);
	}
	

}
