/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.MultiExecuteOutput;
import com.antheminc.oss.nimbus.support.Holder;

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
