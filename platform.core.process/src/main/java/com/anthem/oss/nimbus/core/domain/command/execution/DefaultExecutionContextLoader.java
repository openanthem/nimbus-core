/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.Optional;

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
import com.anthem.oss.nimbus.core.session.Cache;
import com.anthem.oss.nimbus.core.session.impl.SessionCache;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultExecutionContextLoader implements ExecutionContextLoader {

	private final DomainConfigBuilder domainConfigBuilder;
	private final CommandExecutor<?> executorActionNew;
	private final CommandExecutor<?> executorActionGet;

	private final Cache<String, ExecutionContext> sessionCache;
	
	private final QuadModelBuilder quadModelBuilder;
	
	public DefaultExecutionContextLoader(BeanResolverStrategy beanResolver) {
		this.domainConfigBuilder = beanResolver.get(DomainConfigBuilder.class);
		this.quadModelBuilder = beanResolver.get(QuadModelBuilder.class);
		
		this.executorActionNew = beanResolver.get(CommandExecutor.class, Action._new.name() + Behavior.$execute.name());
		this.executorActionGet = beanResolver.get(CommandExecutor.class, Action._get.name() + Behavior.$execute.name());
		
		this.sessionCache = beanResolver.get(SessionCache.class);
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
	
	private String getSessionKey(ExecutionContext eCtx) {
		String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
		String ctxId = eCtx.getId();
		
		String key = ctxId +"_sessionId{"+sessionId+"}";
		return key;
	}
	
	private boolean queueExists(ExecutionContext eCtx) {
		return null != sessionCache.get(getSessionKey(eCtx));
	}
	
	private ExecutionContext queueGet(ExecutionContext eCtx) {
		return sessionCache.get(getSessionKey(eCtx));
	}
	
	private boolean queuePut(ExecutionContext eCtx) {
		synchronized (sessionCache) {
			sessionCache.put(getSessionKey(eCtx), eCtx);
		}
		return true;
	}

	private boolean queueRemove(ExecutionContext eCtx) {
		// skip if doesn't exist
		if(!queueExists(eCtx))
			return false;
		
		synchronized (sessionCache) {
			return sessionCache.remove(getSessionKey(eCtx));
		}
	}
	
	@Override
	public void clear() {
		synchronized (sessionCache) {
			sessionCache.clear();	
		}
	}
}
