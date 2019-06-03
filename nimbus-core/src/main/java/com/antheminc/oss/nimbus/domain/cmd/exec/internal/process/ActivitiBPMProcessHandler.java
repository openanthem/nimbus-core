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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal.process;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.domain.bpm.ProcessEngineContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.FunctionHandler;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

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
