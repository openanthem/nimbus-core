/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.InvalidStateException;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;
import com.anthem.oss.nimbus.core.entity.process.ProcessFlow;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionExecutorGet extends AbstractFunctionCommandExecutor<Param<Object>, Object> {

	private CommandExecutorGateway commandGateway;
	
	private DomainConfigBuilder domainConfigBuilder;
	
	public DefaultActionExecutorGet(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		
		this.commandGateway = getBeanResolver().find(CommandExecutorGateway.class);
		this.domainConfigBuilder = getBeanResolver().find(DomainConfigBuilder.class);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	protected final Output<Object> executeInternal(Input input) {
		ExecutionContext eCtx = handleGetDomainRoot(input.getContext());
	
		Param<?> p = findParamByCommandOrThrowEx(eCtx);
		
		final Object outcome;
		if(containsFunctionHandler(input)) {
			outcome = executeFunctionHanlder(input, FunctionHandler.class);
			
		} else {
			outcome = p;
		}
		
		return Output.instantiate(input, eCtx, outcome);
	}
	
	protected ExecutionContext handleGetDomainRoot(ExecutionContext eCtx) {
		if(eCtx.getQuadModel()!=null)
			return eCtx;

		ModelConfig<?> rootDomainConfig = getRootDomainConfig(eCtx);
		QuadModel<?, ?> q =  createNewQuad(rootDomainConfig, eCtx);
		
		// set to context
		eCtx.setQuadModel(q);
		
		// hook-up BPM
		final ProcessFlow processEntityState = loadProcessState(rootDomainConfig, eCtx);
		q.getRoot().getState().setFlow(processEntityState);
		
		return eCtx;
	}
	
	protected QuadModel<?, ?> createNewQuad(ModelConfig<?> rootDomainConfig, ExecutionContext eCtx) {
		final String refId = eCtx.getCommandMessage().getCommand().getRefId(Type.DomainAlias);
		
		final Object entity;
		final Repo repo = rootDomainConfig.getRepo();
		final String resolvedRepAlias = resolveEntityAliasByRepo(rootDomainConfig);
		
		// db - entity
		if(Repo.Database.exists(repo) && StringUtils.isNotBlank(refId)) { // root (view or core) is persistent
			entity = getRepositoryFactory().get(rootDomainConfig.getRepo())
						._get(refId, rootDomainConfig.getReferredClass(), resolvedRepAlias, eCtx.getCommandMessage().getCommand().getAbsoluteUri());
			
		} else {
			entity = instantiateEntity(eCtx, rootDomainConfig);
		}

		if(rootDomainConfig.isMapped()) 
			return handleMapped(rootDomainConfig, eCtx, entity, Action._get);
		
		// create quad-model
		ExecutionEntity<?, ?> e = ExecutionEntity.resolveAndInstantiate(entity, null);
		
		return getQuadModelBuilder().build(eCtx.getCommandMessage().getCommand(), e);
	}

	protected QuadModel<?, ?> handleMapped(ModelConfig<?> rootDomainConfig, ExecutionContext eCtx, Object mapped, Action action) {
		ModelConfig<?> mapsToConfig = rootDomainConfig.findIfMapped().getMapsTo();

		// create mapsTo command and invoke command gateway to load coreParam
		Command mapsToCmd = CommandBuilder.from(eCtx.getCommandMessage().getCommand(), mapsToConfig.getAlias()).getCommand();
		mapsToCmd.setAction(action);
		
		Param<?> coreParam = Optional.ofNullable(commandGateway.execute(mapsToCmd, null))
								.map(mOut->(Param<?>)mOut.getSingleResult())
								.orElseThrow(()->new InvalidStateException("Expeceted first response from command gateway to return mapsTo core parm, but not found for mapsToCmd: "+mapsToCmd));
		
		return getQuadModelBuilder().build(eCtx.getCommandMessage().getCommand(), mapped, coreParam);
	}
	
	protected ProcessFlow loadProcessState(ModelConfig<?> rootDomainConfig, ExecutionContext eCtx) {
		String domainLifeCycle = rootDomainConfig.getDomainLifecycle();
		
		if(StringUtils.trimToNull(domainLifeCycle)==null)
			return null;
		
		ModelConfig<?> modelConfig = domainConfigBuilder.getModel(ProcessFlow.class);
		Repo repo = modelConfig.getRepo();
		
		if(!Repo.Database.exists(repo))
			throw new InvalidConfigException(ProcessFlow.class.getSimpleName()+" must have @Repo configured for db persistence, but found none.");
		
		String processStateAlias = StringUtils.isBlank(repo.alias()) ? modelConfig.getAlias() : repo.alias();
		
		final String resolvedEntityAlias = resolveEntityAliasByRepo(rootDomainConfig);
		String entityProcessAlias = resolvedEntityAlias + "_" + processStateAlias;
		
		final String entityRefId = eCtx.getCommandMessage().getCommand().getRefId(Type.DomainAlias);
		ProcessFlow processEntityState = getRepositoryFactory().get(repo)._get(entityRefId, ProcessFlow.class, entityProcessAlias);
		return processEntityState;
	}
}