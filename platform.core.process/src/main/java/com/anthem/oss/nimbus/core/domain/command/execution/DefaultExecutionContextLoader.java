/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.web.context.request.RequestContextHolder;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultExecutionContextLoader implements ExecutionContextLoader, HttpSessionListener {

	private final DomainConfigBuilder domainConfigBuilder;
	private final CommandExecutor<?> executorActionNew;
	private final CommandExecutor<?> executorActionGet;

	// TODO: Temp impl till Session is rolled out
	private final SessionData sessionCache;
	
	private final QuadModelBuilder quadModelBuilder;
	
	public DefaultExecutionContextLoader(BeanResolverStrategy beanResolver) {
		this.domainConfigBuilder = beanResolver.get(DomainConfigBuilder.class);
		this.quadModelBuilder = beanResolver.get(QuadModelBuilder.class);
		
		this.executorActionNew = beanResolver.get(CommandExecutor.class, Action._new.name() + Behavior.$execute.name());
		this.executorActionGet = beanResolver.get(CommandExecutor.class, Action._get.name() + Behavior.$execute.name());
		
		// TODO: Temp impl till Session is rolled out
		this.sessionCache = new SessionData();
	}
	
	@Override
	public final ExecutionContext load(Command rootDomainCmd) {
		ExecutionContext eCtx = new ExecutionContext(rootDomainCmd);
		
		// _search: transient - just create shell 
		if(isTransient(rootDomainCmd)) {
			QuadModel<?, ?> q = quadModelBuilder.build(rootDomainCmd);
			eCtx.setQuadModel(q);
			
		} else // _new takes priority
		if(rootDomainCmd.isRootDomainOnly() && rootDomainCmd.getAction()==Action._new) {
			eCtx = loadEntity(eCtx, executorActionNew);
			
		} else // check if already exists in session
		if(sessionExists(eCtx)) { 
			QuadModel<?, ?> q = sessionGet(eCtx);
			eCtx.setQuadModel(q);
			
		} else { // all else requires resurrecting entity
			eCtx = loadEntity(eCtx, executorActionGet);
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
		
		ModelConfig<?> rootDomainConfig = domainConfigBuilder.getRootDomainOrThrowEx(cmdMsg.getCommand().getRootDomainAlias());
		
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
	
	private static final InheritableThreadLocal<String> TH_SESSION = new InheritableThreadLocal<String>() {
		@Override
		protected String initialValue() {
			return RequestContextHolder.getRequestAttributes().getSessionId();
		}
	};
	
	
	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		String sessionId = sessionEvent.getSession().getId();
		sessionCache.getData().put(sessionId, new SessionEntries());
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		String sessionId = sessionEvent.getSession().getId();
		
		SessionEntries entries = sessionCache.getData().remove(sessionId);
		Optional.ofNullable(entries)
			.ifPresent(SessionEntries::clear);
	}
	
	@Getter
	public static class SessionData {
		private final Map<String, SessionEntries> data = new HashMap<>();
		
		private String getCurrentSessionId() {
			return TH_SESSION.get();
		}
		
		public boolean containsKey(ExecutionContext eCtx) {
			String sessionId = getCurrentSessionId();
			
			if(!data.containsKey(sessionId))
				return false;
			
			SessionEntries entries = data.get(sessionId);
			return entries.contains(eCtx);
		}
		
		public ExecutionContext get(ExecutionContext eCtx) {
			String sessionId = getCurrentSessionId();
			
			SessionEntries entries = data.get(sessionId);
			return entries.get(eCtx);
		}
		
		public void put(ExecutionContext eCtx) {
			String sessionId = getCurrentSessionId();
			
			SessionEntries entries;
			if(data.containsKey(sessionId))
				entries = data.get(sessionId);
			else {
				entries = new SessionEntries();
				data.put(sessionId, entries);
			}
			
			entries.put(eCtx);
		}
		
		public ExecutionContext remove(ExecutionContext eCtx) {
			String sessionId = getCurrentSessionId();
			
			SessionEntries entries = data.get(sessionId);
			return entries.remove(eCtx);
		}
		
		public void clear() {
			getData().values().stream().forEach(SessionEntries::clear);
			
			// map clear
			getData().clear();
		}
	}
	
	@Getter
	public static class SessionEntries {
		private final Map<String, ExecutionContext> contexts = new HashMap<>();
		
		public boolean contains(ExecutionContext eCtx) {
			String id = eCtx.getId();
			return contexts.containsKey(id);
		}
		
		public ExecutionContext get(ExecutionContext eCtx) {
			String id = eCtx.getId();
			return contexts.get(id);
		}
		
		public void put(ExecutionContext eCtx) {
			String id = eCtx.getId();
			contexts.put(id, eCtx);
		}
		
		public ExecutionContext remove(ExecutionContext eCtx) {
			String id = eCtx.getId();
			return contexts.remove(id);
		}
		
		public void clear() {
			getContexts().values().stream().forEach(eCtx->{
				eCtx.getQuadModel().getRoot().getExecutionRuntime().stop();
			});
			
			// map clear
			getContexts().clear();
		}
	}
	
	private boolean queueExists(ExecutionContext eCtx) {
		return sessionCache.containsKey(eCtx);
	}
	
	private ExecutionContext queueGet(ExecutionContext eCtx) {
		return sessionCache.get(eCtx);
	}
	
	private boolean queuePut(ExecutionContext eCtx) {
		synchronized (sessionCache) {
			sessionCache.put(eCtx);
		}
		return true;
	}

	private boolean queueRemove(ExecutionContext eCtx) {
		// skip if doesn't exist
		if(!queueExists(eCtx))
			return false;
		
		synchronized (sessionCache) {
			ExecutionContext removed = sessionCache.remove(eCtx);
			return removed!=null;
		}
	}
	
	@Override
	public void clear() {
		synchronized (sessionCache) {
			sessionCache.clear();
		}
	}
}
