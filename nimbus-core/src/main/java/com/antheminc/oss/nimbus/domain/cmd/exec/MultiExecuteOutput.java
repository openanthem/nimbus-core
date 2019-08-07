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

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString 
public class MultiExecuteOutput extends ExecuteOutput<Map<Integer, ExecuteOutput.BehaviorExecute<?>>> {

	private static final long serialVersionUID = 1L;

	private final String sessionId;
	
	public MultiExecuteOutput(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public MultiExecuteOutput(String sessionId, ExecuteOutput.BehaviorExecute<?> output) {
		this.sessionId = sessionId;
		this.add(output);
	}
	
	
	/**
	 * 
	 * @param output
	 */
	public void add(ExecuteOutput.BehaviorExecute<?> output) {
		createOrGet().put(createOrGet().size(), output);
	}
	
	/**
	 * 
	 * @return
	 */
	private Map<Integer, ExecuteOutput.BehaviorExecute<?>> createOrGet() {
		if(getResult() == null) {
			Map<Integer, ExecuteOutput.BehaviorExecute<?>> result = new HashMap<>();
			setResult(result);
		} 
		return getResult();	
	}
	
	@SuppressWarnings("unchecked")
	@JsonIgnore
	public <T> T getSingleResult() {
		if(getResult() == null) return null;
		
		if(getResult().size() > 1) throw new IllegalStateException("Execute output contains more than one result elements: "+getResult());
		
		return (T) this.getResult().values().iterator().next().getResult();
	}
	
}
