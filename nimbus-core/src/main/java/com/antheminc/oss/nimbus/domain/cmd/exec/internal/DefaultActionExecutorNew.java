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

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.bpm.BPMGateway;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.AbstractCommandExecutor;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ValueAccessor;
import com.antheminc.oss.nimbus.domain.model.state.InvalidStateException;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.domain.model.state.internal.ExecutionEntity;
import com.antheminc.oss.nimbus.entity.process.ProcessFlow;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandlerUtils;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@EnableLoggingInterceptor
@Getter(value=AccessLevel.PROTECTED)
public class DefaultActionExecutorNew extends AbstractCommandExecutor<Param<?>> {

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
		setStateNew(eCtx, input.getContext().getCommandMessage(), actionParam);
		return Output.instantiate(input, eCtx, actionParam);
	}

	protected void setStateNew(ExecutionContext eCtx, CommandMessage cmdMsg, Param<Object> p) {
		// skip if call is for domain-root with no payload, as the new entity state would have been instantiated by repo & set prior
		if(!cmdMsg.hasPayload() && cmdMsg.getCommand().isRootDomainOnly())
			return;
		
		Object newState = cmdMsg.hasPayload()
							? getConverter().toReferredType(p.getConfig(), cmdMsg.getRawPayload())
									: getJavaBeanHandler().instantiate(p.getConfig().getReferredClass());
		
		// for /domain-root/_new - set "id" from repo 
		if(cmdMsg.getCommand().isRootDomainOnly()) {
			String idParamCode = p.findIfNested().getIdParam().getConfig().getCode();
			ValueAccessor va = JavaBeanHandlerUtils.constructValueAccessor(p.getConfig().getReferredClass(), idParamCode);
			getJavaBeanHandler().setValue(va, newState, cmdMsg.getCommand().getRootDomainElement().getRefId());
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
		Long refId = getRootDomainRefIdByRepoDatabase(rootDomainConfig, e);
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
		e.setNew(true);
		
		updateCommandWithRefId(rootDomainConfig, eCtx, e);
		
		return getQuadModelBuilder().build(eCtx.getCommandMessage().getCommand(), e);
	}
	
	protected QuadModel<?, ?> handleMapped(ModelConfig<?> rootDomainConfig, ExecutionContext eCtx, Object mapped, Action action) {
		ModelConfig<?> mapsToConfig = rootDomainConfig.findIfMapped().getMapsToConfig();

		Command mapsToCmd = CommandBuilder.from(eCtx.getCommandMessage().getCommand(), mapsToConfig.getAlias()).getCommand();
		mapsToCmd.setAction(action);
		
		Param<?> coreParam = Optional.ofNullable(getCommandGateway().execute(mapsToCmd, null))
								.map(mOut->(Param<?>)mOut.getSingleResult())
								.orElseThrow(()->new InvalidStateException(""));
		
		ExecutionEntity<?, ?> e = ExecutionEntity.resolveAndInstantiate(mapped, coreParam.getState());
		e.setNew(true);
		
		updateCommandWithRefId(rootDomainConfig, eCtx, e);
		
		return getQuadModelBuilder().build(eCtx.getCommandMessage().getCommand(), mapped, coreParam);
	}
	
	
	private void updateCommandWithRefId(ModelConfig<?> rootDomainConfig, ExecutionContext eCtx, ExecutionEntity<?, ?> e) {
		Long refId = getRootDomainRefIdByRepoDatabase(rootDomainConfig, e);
		eCtx.getCommandMessage().getCommand().getRootDomainElement().setRefId(refId);
	}
	
	private ProcessFlow startBusinessProcess(Param<?> rootDomainParam){
		String lifecycleKey = rootDomainParam.findIfNested().getConfig().getDomainLifecycle();
		
		if(StringUtils.isEmpty(lifecycleKey))
			return null;
		return getBpmGateway().startBusinessProcess(rootDomainParam, lifecycleKey);

	}
	
	protected void saveProcessState(String resolvedEntityAlias, ProcessFlow processEntityState) {
		ModelConfig<?> modelConfig = getDomainConfigBuilder().getModel(ProcessFlow.class);
		Repo repo = modelConfig.getRepo();
		
		if(!Repo.Database.exists(repo))
			throw new InvalidConfigException(ProcessFlow.class.getSimpleName()+" must have @Repo configured for db persistence, but found none.");
		
		String processStateAlias = StringUtils.isBlank(repo.alias()) ? modelConfig.getAlias() : repo.alias();
		
		String entityProcessAlias = resolvedEntityAlias + "_" + processStateAlias;
		
		getRepositoryFactory().get(repo)._save(entityProcessAlias, processEntityState);
	}
	
}