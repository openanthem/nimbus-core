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
package com.antheminc.oss.nimbus.domain.model.state.repo;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessageConverter;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
/**
 * @author Sandeep Mantha
 *
 */
public class DefaultParamStateRepositoryStrategy implements ParamStateRepository{

	private ParamStateRepository session;

	private ParamStateRepository local;

	private CommandExecutorGateway commandGateway;

	private CommandMessageConverter commandMessageConverter;
	public DefaultParamStateRepositoryStrategy(ParamStateRepository local, BeanResolverStrategy beanResolver) {
		this.local = local;
		this.commandGateway = beanResolver.get(CommandExecutorGateway.class);
		this.commandMessageConverter = beanResolver.get(CommandMessageConverter.class);
	}
	/**
	 * Local is always kept, but follows behind cache if configured.
	 * 
	 * 1. If cache=true, then retrieve state from cache AND set to local before returning if local state is different
	 * 2. If cache=false, then 
	 */
	@Override
	public <P> P _get(Param<P> param) {	
		if(param.isDomainRoot()) {
			Command cmd = param.getRootExecution().getRootCommand();
			Long refId = cmd.getRefId(Type.DomainAlias);
			ModelConfig<?> config = param.getRootDomain().getConfig();
			if(config.getLock() != null && refId!= null) {
				String alias = StringUtils.isBlank(config.getLock().alias())? cmd.getRootDomainAlias():config.getLock().alias();
				try {
					Config[] createLockConfig = param.getRootDomain().getConfig().getLock().executeToAcquireLock();
					executeConfigs(cmd, createLockConfig, refId, alias);
					executeConfigs(cmd, param.getRootDomain().getConfig().getLock().executeWhenLockAcquired());
				} catch(OptimisticLockingFailureException e) {
					executeConfigs(cmd, param.getRootDomain().getConfig().getLock().executeWhenLockNotAcquired());
					throw new FrameworkRuntimeException(e);
				} catch(DuplicateKeyException e) {
					executeConfigs(cmd, param.getRootDomain().getConfig().getLock().executeWhenLockNotAcquired());
					throw new FrameworkRuntimeException(e);
				}
			}
		}
		if(isCacheable()) {
			P cachedState = session._get(param);
			P localState  = local._get(param);
			if(_equals(cachedState, localState) != null) {
				local._set(param, cachedState);
				localState = cachedState;
			}
			return localState;
		} else {
			P localState  = local._get(param);
			return localState;
		}
	}

	@Override
	public <P> Action _set(Param<P> param, P newState) {
		P currState = _get(param);
		if(_equals(newState, currState) == null) return null;

		if(isCacheable()) {
			session._set(param, newState);
		}
		//_updateParatmStateTree(param, newState);
		return local._set(param, newState);
	}

	public boolean isCacheable() {
		return false;
	}

	@Override
	public String toString() {
		return "Default Strategy";
	}

	public static <P> Action _equals(P newState, P currState) {
		boolean isEqual;
		//00
		if(newState==null && currState==null) return null;

		//01
		if(newState==null && currState!=null) return Action._delete;

		//10
		if(newState!=null && currState==null) return Action._new;

		//11
		if(currState instanceof Object[] && newState instanceof Object[]) {
			isEqual = Arrays.equals((Object[]) currState, (Object[]) newState); 
		} else {
			isEqual = currState.equals(newState);
		}

		return isEqual ? null : Action._update;
	}
	
	private void executeConfigs(Command command, Config[] configList, Long refId, String alias) {
		String marker = command.buildUri(Type.AppAlias);
		LockPayload payload = new LockPayload();
		payload.setAlias(alias);
		payload.setRefId(refId);
		for (Config config : configList) {
			Command cmd = CommandBuilder.withUri(marker + config.url()).getCommand();
			this.commandGateway.execute(cmd, commandMessageConverter.toJson(payload));
		}
	}
	private void executeConfigs(Command command, Config[] configList) {
		String marker = command.buildUri(Type.AppAlias);
		for (Config config : configList) {
			Command cmd = CommandBuilder.withUri(marker + config.url()).getCommand();
			this.commandGateway.execute(cmd, cmd.getRawPayload());
		}
	}
}