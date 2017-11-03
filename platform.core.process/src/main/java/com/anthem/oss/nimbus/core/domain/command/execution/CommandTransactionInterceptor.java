/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.domain.command.Behavior;

/**
 * @author Soham Chakravarti
 *
 */
public class CommandTransactionInterceptor {

	public MultiExecuteOutput handleResponse(MultiExecuteOutput rawResult) {
		return rawResult;
	}
	
	public MultiExecuteOutput handleResponse(ExecuteOutput.BehaviorExecute<?> rawResult) {
		return new MultiExecuteOutput(rawResult);
	}
	
	public MultiExecuteOutput handleResponse(ExecuteOutput<?> rawResult) {
		// This was done to handle a scenario where the result of the executeOutput had a state = MultiExecuteOutput
		// in that case, it was wrapping the response into the outer EXECUTE behavior due to this implementation. 
		// so checking if the result is of Type Holder then calling the handleResponse(Holder) method
		if(rawResult.getResult() instanceof Holder) {
			return handleResponse(((Holder)rawResult.getResult()));
		}
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
