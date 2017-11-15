/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.beans.PropertyDescriptor;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.bpm.BPMGateway;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
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
public class DefaultActionExecutorNew extends AbstractFunctionCommandExecutor<Object, Param<?>> {

	private BPMGateway bpmGateway;
	
	private CommandExecutorGateway commandGateway;
	
	private DomainConfigBuilder domainConfigBuilder;
	
	public DefaultActionExecutorNew(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		
		this.bpmGateway = beanResolver.get(BPMGateway.class);
		this.commandGateway = getBeanResolver().find(CommandExecutorGateway.class);
		this.domainConfigBuilder = getBeanResolver().find(DomainConfigBuilder.class);
	}
	
	/**
	 * 1. If the command is domain root only, then create new instance <br>
	 * <tab>	1.1. Check if payload contains json for initial object to be inserted; convert if available
	 * <tab>	1.2. Else, create new instance and call rep to persist
	 * <tab>	1.3. Update command with domain root refId	
	 * 2. Else, use the payload of command message json to convert & instantiate desired object <br>
	 * <tab>	2.1. Traverse object model path using command domain uri <br>
	 * <tab>	2.2. Set newly instantiated object and return  	
	 */
	@Override
	protected Output<Param<?>> executeInternal(Input input) {
		ExecutionContext eCtx = handleNewDomainRoot(input.getContext());
	
		Param<Object> actionParam = findParamByCommandOrThrowEx(eCtx);
		
		final Param<?> outputParam;
		if(containsFunctionHandler(input)) {
			outputParam = executeFunctionHanlder(input, FunctionHandler.class);
		} else { 
			setStateNew(eCtx, input.getContext().getCommandMessage(), actionParam);
			outputParam = actionParam;
		}
		return Output.instantiate(input, eCtx, outputParam);
	}

	protected void setStateNew(ExecutionContext eCtx, CommandMessage cmdMsg, Param<Object> p) {
		// skip if call is for domain-root with no payload, as the new entity state would have been instantiated by repo & set prior
		if(!cmdMsg.hasPayload() && cmdMsg.getCommand().isRootDomainOnly())
			return;
		
		Object newState = cmdMsg.hasPayload()
							? getConverter().convert(p.getConfig(), cmdMsg.getRawPayload())
									: getJavaBeanHandler().instantiate(p.getConfig().getReferredClass());
		
		// for /domain-root/_new - set "id" from repo 
		if(cmdMsg.getCommand().isRootDomainOnly()) {
			PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(p.getConfig().getReferredClass(), p.getConfig().getCode());
			getJavaBeanHandler().setValue(pd, newState, cmdMsg.getCommand().getRootDomainElement().getRefId());
		}
										
		p.setState(newState);
	}
	
	protected ExecutionContext handleNewDomainRoot(ExecutionContext eCtx) {
		if(eCtx.getQuadModel()!=null)
			return eCtx;
		
		// create new instance of entity and quad
		ModelConfig<?> rootDomainConfig = getRootDomainConfig(eCtx);
		QuadModel<?, ?> q =  createNewQuad(rootDomainConfig, eCtx);
		
		// set to context
		eCtx.setQuadModel(q);
		
		// hook up BPM
		Param<?> rootDomainParam = getRootDomainParam(eCtx);
		ProcessFlow processEntityState = startBusinessProcess(rootDomainParam);
		
		if(processEntityState==null)
			return eCtx;
		
		ExecutionEntity<?, ?> e = q.getRoot().getState();
		String refId = getRootDomainRefIdByRepoDatabase(rootDomainConfig, e);
		processEntityState.setId(refId);
		
		e.setFlow(processEntityState);
		
		saveProcessState(resolveEntityAliasByRepo(rootDomainConfig), processEntityState);
		
		return eCtx;
	}
	
	private QuadModel<?, ?> createNewQuad(ModelConfig<?> rootDomainConfig, ExecutionContext eCtx) {
		// create new entity instance for core & view
		Object entity = instantiateEntity(eCtx, rootDomainConfig);
		
		// unmapped	
		if(!rootDomainConfig.isMapped())
			return handleUnmapped(rootDomainConfig, eCtx, entity);
		
		// mapped
		return handleMapped(rootDomainConfig, eCtx, entity, Action._new);
	}
	
	private QuadModel<?, ?> handleUnmapped(ModelConfig<?> rootDomainConfig, ExecutionContext eCtx, Object entity) {
		ExecutionEntity<?, ?> e = ExecutionEntity.resolveAndInstantiate(entity, null);
		
		updateCommandWithRefId(rootDomainConfig, eCtx, e);
		
		return getQuadModelBuilder().build(eCtx.getCommandMessage().getCommand(), e);
	}
	
	protected QuadModel<?, ?> handleMapped(ModelConfig<?> rootDomainConfig, ExecutionContext eCtx, Object mapped, Action action) {
		ModelConfig<?> mapsToConfig = rootDomainConfig.findIfMapped().getMapsTo();

		Command mapsToCmd = CommandBuilder.from(eCtx.getCommandMessage().getCommand(), mapsToConfig.getAlias()).getCommand();
		mapsToCmd.setAction(action);
		
		Param<?> coreParam = Optional.ofNullable(commandGateway.execute(mapsToCmd, null))
								.map(mOut->(Param<?>)mOut.getSingleResult())
								.orElseThrow(()->new InvalidStateException(""));
		
		ExecutionEntity<?, ?> e = ExecutionEntity.resolveAndInstantiate(mapped, coreParam.getState());
		updateCommandWithRefId(rootDomainConfig, eCtx, e);
		
		return getQuadModelBuilder().build(eCtx.getCommandMessage().getCommand(), mapped, coreParam);
	}
	
	
	private void updateCommandWithRefId(ModelConfig<?> rootDomainConfig, ExecutionContext eCtx, ExecutionEntity<?, ?> e) {
		String refId = getRootDomainRefIdByRepoDatabase(rootDomainConfig, e);
		eCtx.getCommandMessage().getCommand().getRootDomainElement().setRefId(refId);
	}
	
	private ProcessFlow startBusinessProcess(Param<?> rootDomainParam){
		String lifecycleKey = rootDomainParam.findIfNested().getConfig().getDomainLifecycle();
		
		if(StringUtils.isEmpty(lifecycleKey))
			return null;
		ProcessResponse processResponse = bpmGateway.startBusinessProcess(rootDomainParam, lifecycleKey);
		ProcessFlow processFlow = new ProcessFlow();
		processFlow.setProcessExecutionId(processResponse.getExecutionId());
		processFlow.setProcessDefinitionId(processResponse.getDefinitionId());
		return processFlow;
	}
	
	protected void saveProcessState(String resolvedEntityAlias, ProcessFlow processEntityState) {
		ModelConfig<?> modelConfig = domainConfigBuilder.getModel(ProcessFlow.class);
		Repo repo = modelConfig.getRepo();
		
		if(!Repo.Database.exists(repo))
			throw new InvalidConfigException(ProcessFlow.class.getSimpleName()+" must have @Repo configured for db persistence, but found none.");
		
		String processStateAlias = StringUtils.isBlank(repo.alias()) ? modelConfig.getAlias() : repo.alias();
		
		String entityProcessAlias = resolvedEntityAlias + "_" + processStateAlias;
		
		getRepositoryFactory().get(repo)._save(entityProcessAlias, processEntityState);
	}
	
}