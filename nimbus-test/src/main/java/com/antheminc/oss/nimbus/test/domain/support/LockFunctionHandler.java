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
package com.antheminc.oss.nimbus.test.domain.support;

import java.util.List;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessageConverter;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.FunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.repo.LockPayload;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.LockEntity;

import lombok.Getter;

/**
 * @author Sandeep Mantha
 * Implementing locking by userId
 */
@Getter
public class LockFunctionHandler<T> implements FunctionHandler<T, Object>{

	private final SessionProvider sessionProvider;

	private CommandExecutorGateway commandGateway;
	
	private CommandMessageConverter commandMessageConverter;
	
	public LockFunctionHandler(BeanResolverStrategy beanResolver) {
		this.commandGateway = beanResolver.get(CommandExecutorGateway.class);
		this.sessionProvider = beanResolver.find(SessionProvider.class);
		this.commandMessageConverter = beanResolver.get(CommandMessageConverter.class);
	}

	@Override
	public Object execute(ExecutionContext eCtx, Param<T> actionParameter) {
		CommandMessage cmdMsg = eCtx.getCommandMessage();
		String marker = cmdMsg.getCommand().buildUri(cmdMsg.getCommand().root(), Type.PlatformMarker);
		LockPayload payload = commandMessageConverter.toType(LockPayload.class, cmdMsg.getRawPayload());
		List<LockEntity> p = null;
		Command cmd = CommandBuilder.withUri(marker+"/lock/_search?fn=query&where=lock.alias.eq('"+payload.getAlias()+"').and(lock.refId.eq('"+payload.getRefId()+"'))").getCommand();
		MultiOutput o = commandGateway.execute(new CommandMessage(cmd, null));
		p = (List<LockEntity>) o.getOutputs().get(0).getValue();
		if(p == null || p.size() == 0) {
			//Can insert using mongoOps if needed
			cmd = CommandBuilder.withUri(marker+"/lock/_new?fn=_initEntity&target=/lockedBy&json=\""+sessionProvider.getLoggedInUser().getLoginId()+"\"&target=/alias&json=\""+payload.getAlias()+"\"&target=/refId&json=\""+payload.getRefId()+"\"").getCommand();
			commandGateway.execute(new CommandMessage(cmd, null));
		} else if(p.size() == 1){
			if(!p.get(0).getLockedBy().equals(sessionProvider.getLoggedInUser().getLoginId())) {
				throw new FrameworkRuntimeException("Domain entity: "+ p.get(0).getAlias() + " locked with refId: "+ p.get(0).getRefId());
			}
		}
		return p;
	}
}