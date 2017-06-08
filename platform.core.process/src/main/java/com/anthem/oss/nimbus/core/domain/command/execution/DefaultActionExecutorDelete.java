/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;

/**
 * @author Rakesh Patel
 *
 */
public class DefaultActionExecutorDelete extends AbstractCommandExecutor<Boolean> {
	
	public DefaultActionExecutorDelete(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	@Override
	protected Output<Boolean> executeInternal(Input input) {
		ExecutionContext eCtx = input.getContext();
		
		handleDelete(eCtx);
		
		return Output.instantiate(input, eCtx, Boolean.TRUE);
	}
	
	private void handleDelete(ExecutionContext eCtx) {
		String refId = eCtx.getCommandMessage().getCommand().getRefId(Type.DomainAlias);
		
		ModelConfig<?> rootDomainConfig = getRootDomainConfig(eCtx);
		Repo repo = rootDomainConfig.getRepo();
		
		if(Repo.Database.exists(repo)) {
			 getRepositoryFactory().get(repo)
						._delete(refId, rootDomainConfig.getReferredClass(), rootDomainConfig.getDomainAlias());
		} 
		
		if(rootDomainConfig.isMapped()) {
			ModelConfig<?> mapsToConfig = rootDomainConfig.findIfMapped().getMapsTo();
			Repo mapsToRepo = mapsToConfig.getRepo();
			
			if(Repo.Database.exists(mapsToRepo)) {
				 getRepositoryFactory().get(mapsToRepo)
						._delete(refId, mapsToConfig.getReferredClass(), mapsToConfig.getDomainAlias());
			}

		}
	}
	
} 