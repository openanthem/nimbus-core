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
package com.anthem.oss.nimbus.core.bpm;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;

/**
 * @author Jayant.Chaudhuri
 *
 */
abstract public class AbstractExpressionHelper {
	
	private final CommandExecutorGateway executorGateway;
	
	public AbstractExpressionHelper(BeanResolverStrategy beanResolver) {
		this.executorGateway = beanResolver.get(CommandExecutorGateway.class);
	}
	

	final public Object executeProcess(CommandMessage cmdMsg){
		MultiOutput response = executorGateway.execute(cmdMsg);
		return response.getValue();
	}
	
}
