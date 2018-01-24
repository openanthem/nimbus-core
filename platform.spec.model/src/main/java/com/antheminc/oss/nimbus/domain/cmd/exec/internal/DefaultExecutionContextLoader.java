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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutor;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContextLoader;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultExecutionContextLoader implements ExecutionContextLoader, HttpSessionListener {

	private final DomainConfigBuilder domainConfigBuilder;
	private final CommandExecutor<?> executorActionNew;
	private final CommandExecutor<?> executorActionGet;

	// TODO: Temp impl till Session is rolled out
	private final Map<String, ExecutionContext> sessionCache;
	
	private final QuadModelBuilder quadModelBuilder;
	
	private final SessionProvider sessionProvider;
	
	private static final JustLogit logit = new JustLogit(DefaultExecutionContextLoader.class);
	
	public DefaultExecutionContextLoader(BeanResolverStrategy beanResolver) {
		this.domainConfigBuilder = beanResolver.get(DomainConfigBuilder.class);
		this.quadModelBuilder = beanResolver.get(QuadModelBuilder.class);
		
		this.executorActionNew = beanResolver.get(CommandExecutor.class, Action._new.name() + Behavior.$execute.name());
		this.executorActionGet = beanResolver.get(CommandExecutor.class, Action._get.name() + Behavior.$execute.name());
		
		this.sessionProvider = beanResolver.get(SessionProvider.class);
		
		// TODO: Temp impl till Session is rolled out
		this.sessionCache = new HashMap<>(100);
	}
	
//	private static String getSessionIdForLogging() {
//		final String thSessionId = TH_SESSION.get();
//		try {
//			String msg = "Session from HTTP: "+ RequestContextHolder.getRequestAttributes().getSessionId()+
//							" :: Session  from TH_SESSION: "+ thSessionId;
//			return msg;
//		} catch (Exception ex) {
//			logit.error(()->"Failed to get session info, TH_SESSION: "+thSessionId, ex);
//			return "Failed to get session from HTTP, TH_SESSION: "+thSessionId;
//		}
//	}
	
	@Override
	public ExecutionContext load(Command rootDomainCmd) {
		String sessionId = sessionProvider.getSessionId();
		return load(rootDomainCmd, sessionId);
	}
	@Override
	public final ExecutionContext load(Command rootDomainCmd, String sessionId) {
		logit.trace(()->"[load][I] rootDomainCmd:"+rootDomainCmd+" for "+sessionId);
		
		ExecutionContext eCtx = new ExecutionContext(rootDomainCmd);
		
		// _search: transient - just create shell 
		if(isTransient(rootDomainCmd)) {
			logit.trace(()->"[load] isTransient");
			
			QuadModel<?, ?> q = quadModelBuilder.build(rootDomainCmd);
			eCtx.setQuadModel(q);
			
		} else // _new takes priority
		if(rootDomainCmd.isRootDomainOnly() && rootDomainCmd.getAction()==Action._new) {
			logit.trace(()->"[load] isRootDomainOnly && _new");
			
			eCtx = loadEntity(eCtx, executorActionNew, sessionId);
			
		} else // check if already exists in session
		if(sessionExists(eCtx, sessionId)) { 
			logit.trace(()->"[load] sessionExists");
			
			QuadModel<?, ?> q = sessionGet(eCtx, sessionId);
			eCtx.setQuadModel(q);
			
		} else { // all else requires resurrecting entity
			logit.trace(()->"[load] do _get and put in sessionIfApplicable");
			
			eCtx = loadEntity(eCtx, executorActionGet, sessionId);
		}
		
		logit.trace(()->"[load][O] rootDomainCmd:"+rootDomainCmd+" for "+sessionId);
		return eCtx;
	}
	
	@Override
	public final void unload(ExecutionContext eCtx, String sessionId) {
		sessionRemomve(eCtx, sessionId);
		
		// also do an explicit shutdown
		eCtx.getQuadModel().getRoot().getExecutionRuntime().stop();
	}

	private boolean isTransient(Command cmd) {
		return cmd.getAction()==Action._search 
				|| cmd.getAction()==Action._config;
	}
	
	private ExecutionContext loadEntity(ExecutionContext eCtx, CommandExecutor<?> executor, String sessionId) {
		CommandMessage cmdMsg = eCtx.getCommandMessage();
		String inputCmdUri = cmdMsg.getCommand().getAbsoluteUri();
		
		Input input = new Input(inputCmdUri, eCtx, cmdMsg.getCommand().getAction(), Behavior.$execute);
		Output<?> output = executor.execute(input);
		
		// update context
		eCtx = output.getContext();
		
		ModelConfig<?> rootDomainConfig = domainConfigBuilder.getRootDomainOrThrowEx(cmdMsg.getCommand().getRootDomainAlias());
		
		sessionPutIfApplicable(rootDomainConfig, eCtx, sessionId);
		
		return eCtx;
	}
	
	protected boolean sessionPutIfApplicable(ModelConfig<?> rootDomainConfig, ExecutionContext eCtx, String sessionId) {
		Repo repo = rootDomainConfig.getRepo();
		if(repo==null)
			return false;
		
		if(repo.cache()==Repo.Cache.rep_device) {
			return queuePut(eCtx, sessionId);
		}

		return false;
	}
	
	protected boolean sessionRemomve(ExecutionContext eCtx, String sessionId) {
		return queueRemove(eCtx, sessionId);
	}
	
	private void logSessionKeys() {
		logit.trace(()->"session size: "+sessionCache.size());
		
		sessionCache.keySet().stream()
			.forEach(key->logit.trace(()->"session key: "+key));
	}
	
	
	protected boolean sessionExists(ExecutionContext eCtx, String sessionId) {
		return queueExists(eCtx, sessionId);
	}
	
	protected QuadModel<?, ?> sessionGet(ExecutionContext eCtx, String sessionId) {
		return Optional.ofNullable(queueGet(eCtx, sessionId))
				.map(ExecutionContext::getQuadModel)
				.orElse(null);
	}
	
	
	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		logit.trace(()->"[sessionCreated] id:"+httpSessionEvent.getSession().getId());
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		logit.trace(()->"[sessionDestroyed] id:"+httpSessionEvent.getSession().getId());
		
		String sessionKeyPart = getSessionKeyPart(httpSessionEvent.getSession().getId());
		
		List<String> keysToRemove = new ArrayList<>();
		sessionCache.keySet().stream()
			.filter(key->key.startsWith(sessionKeyPart))
			.forEach(keysToRemove::add);
		
		List<ExecutionContext> removedCtxs = new ArrayList<>(keysToRemove.size());
		synchronized(sessionCache) {
			keysToRemove.stream()
				.map(sessionCache::remove)
				.forEach(removedCtxs::add);
		}
		
		removedCtxs.stream()
			.forEach(e->e.getQuadModel().getRoot().getExecutionRuntime().stop());
	}
	
	private String getSessionKey(ExecutionContext eCtx, String sessionId) {
		logit.trace(()->"[getSessionKey] eCtx:"+eCtx+" for "+sessionId);
		logSessionKeys();
	
		String ctxId = eCtx.getId();
		
		String key = getSessionKeyPart(sessionId) + ctxId;
		return key;
	}
	
	private String getSessionKeyPart(String sessionId) {
		return "_sessionId{"+sessionId+"}";
	}
	
	private boolean queueExists(ExecutionContext eCtx, String sessionId) {
		return sessionCache.containsKey(getSessionKey(eCtx, sessionId));
	}
	
	private ExecutionContext queueGet(ExecutionContext eCtx, String sessionId) {
		return sessionCache.get(getSessionKey(eCtx, sessionId));
	}
	
	private boolean queuePut(ExecutionContext eCtx, String sessionId) {
		synchronized (sessionCache) {
			sessionCache.put(getSessionKey(eCtx, sessionId), eCtx);
		}
		return true;
	}

	private boolean queueRemove(ExecutionContext eCtx, String sessionId) {
		// skip if doesn't exist
		if(!queueExists(eCtx, sessionId))
			return false;
		
		synchronized (sessionCache) {
			ExecutionContext removed = sessionCache.remove(getSessionKey(eCtx, sessionId));
			return removed!=null;
		}
	}
	
	@Override
	public void clear() {
		synchronized (sessionCache) {
			// shutdown
			sessionCache.values().stream()
				.forEach(e->{
					e.getQuadModel().getRoot().getExecutionRuntime().stop();
				});
			
			// clear cache
			sessionCache.clear();	
		}
	}
}
