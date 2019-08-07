/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.domain.cmd.exec;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.support.Holder;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter(value=AccessLevel.PROTECTED)
public class CommandTransactionInterceptor {

	private final SessionProvider sessionProvider;
	
	public CommandTransactionInterceptor(BeanResolverStrategy beanResolver) {
		this.sessionProvider = beanResolver.get(SessionProvider.class);
	}
	
	public MultiExecuteOutput handleResponse(MultiExecuteOutput rawResult) {
		return rawResult;
	}
	
	public MultiExecuteOutput handleResponse(ExecuteOutput.BehaviorExecute<?> rawResult) {
		return new MultiExecuteOutput(getSessionProvider().getSessionId(), rawResult);
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
		
		MultiExecuteOutput mExecOutput = new MultiExecuteOutput(getSessionProvider().getSessionId(), new ExecuteOutput.BehaviorExecute<>(Behavior.$execute, rawResult));
		return handleResponse(mExecOutput);
	}
	

}
