/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution;

import javax.annotation.PostConstruct;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.CommandElement.Type;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.ListElemParam;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Rakesh Patel
 * @author Soham Chakravarti
 */
public class DefaultActionExecutorDelete extends AbstractCommandExecutor<Boolean> {
	
	private ExecutionContextLoader loader;
	
	public DefaultActionExecutorDelete(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	@PostConstruct
	public void initDependencies() {
		this.loader = getBeanResolver().get(ExecutionContextLoader.class);
	}
	
	
	
	@Override
	protected Output<Boolean> executeInternal(Input input) {
		ExecutionContext eCtx = input.getContext();
		
		Param<Object> p = findParamByCommandOrThrowEx(eCtx);
		
		// handle domain root only 
		if(eCtx.getCommandMessage().getCommand().isRootDomainOnly()) {
			handleRootDelete(eCtx);
			
			loader.unload(eCtx, getSessionId());
		}
		else if(p.isCollection())
			handleCollection(eCtx, p.findIfCollection());
		else if(p.isCollectionElem())
			handleCollectionElem(eCtx, p.findIfCollectionElem());
		else
			handleParam(eCtx, p);
			
		
		return Output.instantiate(input, eCtx, Boolean.TRUE);
	}
	
	protected void handleRootDelete(ExecutionContext eCtx) {
		String refId = eCtx.getCommandMessage().getCommand().getRefId(Type.DomainAlias);
		
		ModelConfig<?> rootDomainConfig = getRootDomainConfig(eCtx);
		Repo repo = rootDomainConfig.getRepo();
		
		if(Repo.Database.exists(repo)) {
			 getRepositoryFactory().get(repo)
						._delete(refId, rootDomainConfig.getReferredClass(), rootDomainConfig.getAlias());
		} 
		
		if(rootDomainConfig.isMapped()) {
			ModelConfig<?> mapsToConfig = rootDomainConfig.findIfMapped().getMapsTo();
			Repo mapsToRepo = mapsToConfig.getRepo();
			
			if(Repo.Database.exists(mapsToRepo)) {
				 getRepositoryFactory().get(mapsToRepo)
						._delete(refId, mapsToConfig.getReferredClass(), mapsToConfig.getAlias());
			}

		}
	}
	
	protected void handleCollection(ExecutionContext eCtx, ListParam<Object> p) {
		p.clear();
	}
	
	protected void handleCollectionElem(ExecutionContext eCtx, ListElemParam<Object> p) {
		p.remove();
	}
	
	protected void handleParam(ExecutionContext eCtx, Param<Object> p) {
		p.setState(null);
	}
} 