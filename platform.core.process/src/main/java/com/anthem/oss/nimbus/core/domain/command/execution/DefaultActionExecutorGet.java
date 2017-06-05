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
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionExecutorGet extends AbstractCommandExecutor<Param<?>> {
	
	public DefaultActionExecutorGet(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	@Override
	protected final Output<Param<?>> executeInternal(Input input) {
		ExecutionContext eCtx = handleGetDomainRoot(input.getContext());
	
		Param<?> p = findParamByCommand(eCtx);
		
		return Output.instantiate(input, eCtx, p);
	}
	
	protected ExecutionContext handleGetDomainRoot(ExecutionContext eCtx) {
		if(eCtx.getQuadModel()!=null)
			return eCtx;

		ModelConfig<?> rootDomainConfig = getRootDomainConfig(eCtx);
		QuadModel<?, ?> q =  createNewQuad(rootDomainConfig, eCtx);
		
		// set to context
		eCtx.setQuadModel(q);
		
		return eCtx;
	}
	
	private QuadModel<?, ?> createNewQuad(ModelConfig<?> rootDomainConfig, ExecutionContext eCtx) {
		String refId = eCtx.getCommandMessage().getCommand().getRefId(Type.DomainAlias);
		
		final Object entity;
		final Object mapsToEntity;
//		boolean repoDbFound = false;
		
		if(Repo.Database.exists(rootDomainConfig.getRepo())) { // root (view or core) is persistent
			entity = getRepositoryFactory().get(rootDomainConfig.getRepo())
						._get(refId, rootDomainConfig.getReferredClass(), rootDomainConfig.getDomainAlias());
//			repoDbFound = true;
			
		} else {
			entity = instantiateEntity(rootDomainConfig);
		}
		
		if(rootDomainConfig.isMapped()) {
			ModelConfig<?> mapsToConfig = rootDomainConfig.findIfMapped().getMapsTo();
			Repo mapsToRepo = mapsToConfig.getRepo();
			
			if(Repo.Database.exists(mapsToRepo)) {
				mapsToEntity = getRepositoryFactory().get(rootDomainConfig.getRepo())._get(refId, mapsToConfig.getReferredClass(), mapsToConfig.getDomainAlias());
//				repoDbFound = true;
				
			} else {
				mapsToEntity = instantiateEntity(mapsToConfig);
			}
		} else {
			mapsToEntity = null;
		}
		
//		// ensure that only scenarios configured with persistence is handled here, other flows such as cache-only, should have been handled prior
//		if(!repoDbFound)
//			throw new UnsupportedScenarioException("Non persistence based flows should have been accounted prior to this api call,"
//					+ " Found for root-domain: "+rootDomainConfig+" with execution context: "+eCtx);
//		

		// create quad-model
		ExecutionEntity<?, ?> e = ExecutionEntity.resolveAndInstantiate(entity, mapsToEntity);
		
		return getQuadModelBuilder().build(eCtx.getCommandMessage().getCommand(), e);
		
	}

}
