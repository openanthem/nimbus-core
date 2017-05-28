/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

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

	private final BlockingQueue<ExecutionContext> sessionCache;
	
	private final QuadModelBuilder quadModelBuilder;
	
	private final JustLogit logit = new JustLogit(this.getClass());
	
	public DefaultExecutionContextLoader(BeanResolverStrategy beanResolverStrategy) {
		this.domainConfigBuilder = beanResolverStrategy.get(DomainConfigBuilder.class);
		this.quadModelBuilder = beanResolverStrategy.get(QuadModelBuilder.class);
		
		this.executorActionNew = beanResolverStrategy.get(CommandExecutor.class, Action._new.name() + Behavior.$execute.name());
		this.executorActionGet = beanResolverStrategy.get(CommandExecutor.class, Action._get.name() + Behavior.$execute.name());
		
		// TODO: Temp impl till Session is rolled out
		this.sessionCache = new LinkedBlockingQueue<>(100);
	}
	
	@Override
	public ExecutionContext load(CommandMessage cmdMsg) {
		String inputCmdUri = cmdMsg.getCommand().getAbsoluteUri();
		
		ModelConfig<?> rootDomainConfig = domainConfigBuilder.getRootDomainOrThrowEx(cmdMsg.getCommand().getRootDomainAlias());
		
		ExecutionContext eCtx = new ExecutionContext(cmdMsg);
		
		// _search: transient - just create shell 
		if(isTransient(cmdMsg.getCommand())) {
			QuadModel<?, ?> q = quadModelBuilder.build(cmdMsg.getCommand());
			eCtx.setQuadModel(q);
			
		} else {
			
			Input input = new Input(inputCmdUri, eCtx, cmdMsg.getCommand().getAction(), Behavior.$execute);
			Output<?> output = (cmdMsg.getCommand().getAction() == Action._new) ? executorActionNew.execute(input) : executorActionGet.execute(input);
			
			// update context
			eCtx = output.getContext();
			
			putInSessionIfApplicable(rootDomainConfig, eCtx);
		}
		
		return eCtx;
	}

	private boolean isTransient(Command cmd) {
		return cmd.getAction()==Action._search || cmd.getAction()==Action._lookup;
	}
	
	protected boolean putInSessionIfApplicable(ModelConfig<?> rootDomainConfig, ExecutionContext eCtx) {
		Repo repo = rootDomainConfig.getRepo();
		if(repo==null)
			return false;
		
		if(repo.cache()==Repo.Cache.rep_session) {
			queuePut(eCtx);
			return true;
		}

		return false;
	}
	
	private void queuePut(ExecutionContext eCtx) {
		// skip if exists
		if(sessionCache.contains(eCtx))
			return;
		
		synchronized (sessionCache) {
			if(sessionCache.remainingCapacity()==0) { 
				ExecutionContext removed = sessionCache.remove();
				logit.debug(()->"sessionCache: Found remaining capacity = 0, removed ExecutionContext: "+removed.getId()
				);
			}
			
			sessionCache.add(eCtx);
		}
	}

}
