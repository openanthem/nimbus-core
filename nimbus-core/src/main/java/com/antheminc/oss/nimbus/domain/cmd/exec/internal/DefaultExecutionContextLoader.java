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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import java.util.Optional;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutor;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContextLoader;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.domain.model.state.builder.QuadModelBuilder;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@EnableLoggingInterceptor
@Getter(value=AccessLevel.PROTECTED)
public class DefaultExecutionContextLoader implements ExecutionContextLoader {

	private final DomainConfigBuilder domainConfigBuilder;
	private final CommandExecutor<?> executorActionNew;
	private final CommandExecutor<?> executorActionGet;

	private final QuadModelBuilder quadModelBuilder;
	
	private final SessionProvider sessionProvider;
	
	public DefaultExecutionContextLoader(BeanResolverStrategy beanResolver) {
		this.domainConfigBuilder = beanResolver.get(DomainConfigBuilder.class);
		this.quadModelBuilder = beanResolver.get(QuadModelBuilder.class);
		this.executorActionNew = beanResolver.get(CommandExecutor.class, Action._new.name() + Behavior.$execute.name());
		this.executorActionGet = beanResolver.get(CommandExecutor.class, Action._get.name() + Behavior.$execute.name());
		this.sessionProvider = beanResolver.get(SessionProvider.class);
	}
	

	@Override
	public final ExecutionContext load(Command rootDomainCmd) {
		ExecutionContext eCtx = new ExecutionContext(rootDomainCmd);
		
		// _search: transient - just create shell 
		if(isTransient(rootDomainCmd)) {
			QuadModel<?, ?> q = getQuadModelBuilder().build(rootDomainCmd);
			eCtx.setQuadModel(q);
			
		} else // _new takes priority
		if(rootDomainCmd.isRootDomainOnly() && rootDomainCmd.getAction()==Action._new) {
			eCtx = loadEntity(eCtx, getExecutorActionNew());
			
			// init state post registering of quadModel in session
			eCtx.getRootModel().initState();
			
		} else // check if already exists in session
		if(sessionExists(eCtx)) { 
			QuadModel<?, ?> q = sessionGet(eCtx);
			eCtx.setQuadModel(q);
			
		} else { // all else requires resurrecting entity
			eCtx = loadEntity(eCtx, getExecutorActionGet());
		}
		return eCtx;
	}
	
	@Override
	public final void unload(ExecutionContext eCtx) {
		sessionRemomve(eCtx);
		
		// also do an explicit shutdown
		eCtx.getQuadModel().getRoot().getExecutionRuntime().stop();
	}

	private boolean isTransient(Command cmd) {
		return cmd.getAction()==Action._search 
				|| cmd.getAction()==Action._config;
	}
	
	private ExecutionContext loadEntity(ExecutionContext eCtx, CommandExecutor<?> executor) {
		CommandMessage cmdMsg = eCtx.getCommandMessage();
		String inputCmdUri = cmdMsg.getCommand().getAbsoluteUri();
		
		Input input = new Input(inputCmdUri, eCtx, cmdMsg.getCommand().getAction(), Behavior.$execute);
		Output<?> output = executor.execute(input);
		
		// update context
		eCtx = output.getContext();
		
		ModelConfig<?> rootDomainConfig = getDomainConfigBuilder().getRootDomainOrThrowEx(cmdMsg.getCommand().getRootDomainAlias());
		
		sessionPutIfApplicable(rootDomainConfig, eCtx);
		
		return eCtx;
	}
	
	protected boolean sessionPutIfApplicable(ModelConfig<?> rootDomainConfig, ExecutionContext eCtx) {
		Repo repo = rootDomainConfig.getRepo();
		if(repo==null)
			return false;
		
		if(repo.cache()==Repo.Cache.rep_device) {
			return queuePut(eCtx);
		}

		return false;
	}
	
	protected boolean sessionRemomve(ExecutionContext eCtx) {
		return queueRemove(eCtx);
	}
	
	protected boolean sessionExists(ExecutionContext eCtx) {
		return queueExists(eCtx);
	}
	
	protected QuadModel<?, ?> sessionGet(ExecutionContext eCtx) {
		return Optional.ofNullable(queueGet(eCtx))
				.map(ExecutionContext::getQuadModel)
				.orElse(null);
	}
	
	
	private String getSessionKey(ExecutionContext eCtx) {
		String ctxId = eCtx.getId();
		return new StringBuilder().append("{").append(ctxId).append("}").toString();
	}
	
	private boolean queueExists(ExecutionContext eCtx) {
		return getSessionProvider().getAttribute(getSessionKey(eCtx)) != null;
	}
	
	private ExecutionContext queueGet(ExecutionContext eCtx) {
		return getSessionProvider().getAttribute(getSessionKey(eCtx));
	}
	
	private boolean queuePut(ExecutionContext eCtx) {
		getSessionProvider().setAttribute(getSessionKey(eCtx), eCtx);
		return true;
	}

	private boolean queueRemove(ExecutionContext eCtx) {
		return getSessionProvider().removeAttribute(getSessionKey(eCtx));
	}
	
	@Override
	public void clear() {}
}
