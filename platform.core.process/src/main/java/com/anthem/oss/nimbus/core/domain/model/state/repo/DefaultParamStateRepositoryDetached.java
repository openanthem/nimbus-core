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
package com.anthem.oss.nimbus.core.domain.model.state.repo;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandPathVariableResolver;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.util.JustLogit;

/**
 * @author Rakesh Patel
 *
 */
public class DefaultParamStateRepositoryDetached implements ParamStateRepository {

	private JustLogit logit = new JustLogit(getClass());
	
	protected final CommandExecutorGateway gateway;
	
	protected final CommandPathVariableResolver pathVariableResolver;
	
	public DefaultParamStateRepositoryDetached(BeanResolverStrategy beanResolver) {
		this.gateway = beanResolver.get(CommandExecutorGateway.class);
		this.pathVariableResolver = beanResolver.get(CommandPathVariableResolver.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <P> P _get(Param<P> param) {
		
		String completeUri = param.getRootExecution().getRootCommand().getRelativeUri(param.getConfig().findIfMapped().getPath().value());
		String resolvedUri = this.pathVariableResolver.resolve(param, completeUri);
		Command cmd = CommandBuilder.withUri(resolvedUri).getCommand();
		CommandMessage cmdMsg = new CommandMessage(cmd, null);
		MultiOutput multiOp = this.gateway.execute(cmdMsg);
		Param<P> pState = (Param<P>)multiOp.getSingleResult();
		return pState.getLeafState();
	
	}
	
	@Override
	public <P> Action _set(Param<P> param, P newState) {
		logit.trace(()->"_set@enter -> param.path: "+param.getPath()+" newState: "+newState);
		
		throw new UnsupportedOperationException("The mapped Detached param only supports _get as of now, _set is not implemented. Param path is: "+param.getPath());
	}

}
