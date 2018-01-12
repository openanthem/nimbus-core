/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm;

import com.anthem.oss.nimbus.core.domain.command.execution.ProcessResponse;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.entity.process.ProcessFlow;

/**
 * @author Jayant Chaudhuri
 *
 */
public interface BPMGateway {

	public ProcessFlow startBusinessProcess(Param<?> param,String processId);
	
	public ProcessResponse startStatlessBusinessProcess(Param<?> param,String processId);
	
	public Object continueBusinessProcessExecution(Param<?> param, String processExecutionId);
}
