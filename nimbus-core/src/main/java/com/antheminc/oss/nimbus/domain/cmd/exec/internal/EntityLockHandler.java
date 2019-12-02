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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessageConverter;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Lock;
import com.antheminc.oss.nimbus.domain.defn.Lock.LockedBy;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
public class EntityLockHandler {
	
	private CommandExecutorGateway  commandExecutorGateway;
	private CommandMessageConverter commandMessageConverter;
	private SessionProvider sessionProvider;
	
	public EntityLockHandler(BeanResolverStrategy beanResolver) {
		commandExecutorGateway = beanResolver.find(CommandExecutorGateway.class);
		commandMessageConverter = beanResolver.find(CommandMessageConverter.class);
		sessionProvider = beanResolver.find(SessionProvider.class);
	}
	
	public void createLock(ExecutionContext eCtx,
			ModelConfig<?> rootDomainConfig, String alias, Object refId) {
		Lock lock = rootDomainConfig.getLock();
		Command rootCommand = eCtx.getCommandMessage().getCommand();
		try {
			executeConfigs(rootCommand,lock.executeToAcquireLock(),alias,refId,createLockPayload(lock,alias,refId));
			executeConfigs(rootCommand,lock.executeWhenLockAcquired());
		}catch (OptimisticLockingFailureException e) {
			executeConfigs(rootCommand,lock.executeWhenLockNotAcquired());
			throw new FrameworkRuntimeException(e);
		}catch (DuplicateKeyException e) {
			executeConfigs(rootCommand,lock.executeWhenLockNotAcquired());
			throw new FrameworkRuntimeException(e);
		}
		
	}
	
	private void executeConfigs(Command command, Config[] configList, String alias, Object refId, LockPayload payload) {
		String marker = command.buildUri(Type.AppAlias);
		for (Config config : configList) {
			Command cmd = CommandBuilder.withUri(marker + config.url()).getCommand();
			commandExecutorGateway.execute(cmd, commandMessageConverter.toJson(payload));
		}
	}
	
	private void executeConfigs(Command command, Config[] configList) {
		String marker = command.buildUri(Type.AppAlias);
		for (Config config : configList) {
			Command cmd = CommandBuilder.withUri(marker + config.url()).getCommand();
			commandExecutorGateway.execute(cmd, cmd.getRawPayload());
		}
	}
	
	private LockPayload createLockPayload(Lock lock, String alias, Object refId) {
		LockPayload payload = new LockPayload();
		payload.setAlias(alias);
		payload.setRefId(refId);
		Object lockedBy = null;
		if(lock.lockedBy() == LockedBy.session) {
			lockedBy = sessionProvider.getSessionId();
		}else if(lock.lockedBy() == LockedBy.session) {
			lockedBy = sessionProvider.getLoggedInUser().getLoginId();
		}
		payload.setLockedBy(lockedBy);
		return payload;
		
	}
	
	@Getter @Setter
	public class LockPayload{
		private String alias;
		private Object refId;
		private Object lockedBy;
	}
	
}
