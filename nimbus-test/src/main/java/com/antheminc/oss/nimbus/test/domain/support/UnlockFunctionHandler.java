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

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.FunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.LockEntity;

import lombok.Getter;

/**
 * @author Sandeep Mantha
 *
 */
@Getter
public class UnlockFunctionHandler implements FunctionHandler<Object, Param<Object>>{

	private final SessionProvider sessionProvider;

	private CommandExecutorGateway commandGateway;

	public UnlockFunctionHandler(BeanResolverStrategy beanResolver) {
		this.commandGateway = beanResolver.get(CommandExecutorGateway.class);
		this.sessionProvider = beanResolver.find(SessionProvider.class);
	}

	@Override
	public Param<Object> execute(ExecutionContext eCtx, Param<Object> actionParameter) {
		CommandMessage cmdMsg = eCtx.getCommandMessage();
		String marker = cmdMsg.getCommand().buildUri(cmdMsg.getCommand().root(), Type.PlatformMarker);
		Command cmd = CommandBuilder.withUri(marker+"/lock/_search?fn=query&where=lock.lockedBy.eq('"+sessionProvider.getLoggedInUser().getLoginId() +"')").getCommand();
		MultiOutput o = commandGateway.execute(new CommandMessage(cmd, null));
		List<LockEntity> p = null;
		p = (List<LockEntity>) o.getOutputs().get(0).getValue();
		p.stream().forEach(e-> {
			Command cmd_delete = CommandBuilder.withUri(marker+"/lock:"+e.getId().toString()+"/_delete").getCommand();
			commandGateway.execute(new CommandMessage(cmd_delete, null));
	    });
		return null;
	}

}