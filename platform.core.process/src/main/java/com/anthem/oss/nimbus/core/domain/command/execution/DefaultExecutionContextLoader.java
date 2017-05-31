/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
import com.anthem.oss.nimbus.core.util.JustLogit;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultExecutionContextLoader implements ExecutionContextLoader {

	private final DomainConfigBuilder domainConfigBuilder;
	private final CommandExecutor<?> executorActionNew;
	private final CommandExecutor<?> executorActionGet;

	// TODO: Temp impl till Session is rolled out
	private final BlockingQueue<ExecutionContext> sessionCache;
	
	private final QuadModelBuilder quadModelBuilder;
	
	private final JustLogit logit = new JustLogit(this.getClass());
	
	public DefaultExecutionContextLoader(BeanResolverStrategy beanResolver) {
		this.domainConfigBuilder = beanResolver.get(DomainConfigBuilder.class);
		this.quadModelBuilder = beanResolver.get(QuadModelBuilder.class);
		
		this.executorActionNew = beanResolver.get(CommandExecutor.class, Action._new.name() + Behavior.$execute.name());
		this.executorActionGet = beanResolver.get(CommandExecutor.class, Action._get.name() + Behavior.$execute.name());
		
		// TODO: Temp impl till Session is rolled out
		this.sessionCache = new LinkedBlockingQueue<>(100);
	}
	
	@Override
	public final ExecutionContext load(CommandMessage cmdMsg) {
		ExecutionContext eCtx = new ExecutionContext(cmdMsg);
		
		// _search: transient - just create shell 
		if(isTransient(cmdMsg.getCommand())) {
			QuadModel<?, ?> q = quadModelBuilder.build(cmdMsg.getCommand());
			eCtx.setQuadModel(q);
			
		} else // _new takes priority
		if(cmdMsg.getCommand().getAction()==Action._new) {
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

	private boolean isTransient(Command cmd) {
		return cmd.getAction()==Action._search || cmd.getAction()==Action._lookup;
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
		
		if(repo.cache()==Repo.Cache.rep_session) {
			return queuePut(eCtx);
		}

		return false;
	}
	
	protected boolean sessionExists(ExecutionContext eCtx) {
		return queueExists(eCtx);
	}
	
	protected QuadModel<?, ?> sessionGet(ExecutionContext eCtx) {
		return Optional.ofNullable(queueGet(eCtx.getCommandMessage().getCommand()))
				.map(ExecutionContext::getQuadModel)
				.orElse(null);
	}
	
	private boolean queueExists(ExecutionContext eCtx) {
		return sessionCache.contains(eCtx);
	}
	
	private ExecutionContext queueGet(Command cmd) {
		return sessionCache.stream()
				.filter(e->e.equalsId(cmd))
				.findFirst()
				.orElse(null);
	}
	
	private boolean queuePut(ExecutionContext eCtx) {
		// skip if exists
		if(queueExists(eCtx))
			return false;
		
		synchronized (sessionCache) {
			if(sessionCache.remainingCapacity()==0) { 
				ExecutionContext removed = sessionCache.remove();
				logit.debug(()->"sessionCache: Found remaining capacity = 0, removed ExecutionContext: "+removed.getId()
				);
			}
			
			sessionCache.add(eCtx);
		}
		return true;
	}

}
