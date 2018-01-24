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
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values;
import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values.EMPTY;
import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values.Source;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;
import com.anthem.oss.nimbus.core.rules.RulesEngineFactoryProducer;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandPathVariableResolver;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.defn.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Mode;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamType;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig.MappedParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.StateType;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultListElemParamState;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultListModelState;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultModelState;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultParamState;
import com.antheminc.oss.nimbus.domain.model.state.internal.ExecutionEntity;
import com.antheminc.oss.nimbus.domain.model.state.internal.MappedDefaultTransientParamState;
import com.antheminc.oss.nimbus.domain.model.state.internal.ExecutionEntity.ExModelConfig;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.antheminc.oss.nimbus.support.pojo.ClassLoadUtils;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
abstract public class AbstractEntityStateBuilder extends AbstractEntityStateFactoryProducer {

	protected final RulesEngineFactoryProducer rulesEngineFactoryProducer;
	
	protected final CommandExecutorGateway gateway;
	
	protected CommandPathVariableResolver pathVariableResolver;
	
	protected JustLogit logit = new JustLogit(getClass());
	
	public AbstractEntityStateBuilder(BeanResolverStrategy beanResolver) {
		this.rulesEngineFactoryProducer = beanResolver.get(RulesEngineFactoryProducer.class);
		this.gateway = beanResolver.get(CommandExecutorGateway.class);
		this.pathVariableResolver = beanResolver.get(CommandPathVariableResolver.class);
	}
	
	abstract public <T, P> DefaultModelState<T> buildModel(EntityStateAspectHandlers provider, DefaultParamState<T> associatedParam, ModelConfig<T> mConfig, Model<?> mapsToSAC);
	abstract public <T, P> DefaultParamState<P> buildParam(EntityStateAspectHandlers provider, Model<T> mState, ParamConfig<P> mpConfig, Model<?> mapsToSAC);
	abstract protected <P> StateType buildParamType(EntityStateAspectHandlers provider, DefaultParamState<P> associatedParam, Model<?> mapsToSAC);
	
	protected <T> DefaultModelState<T> createModel(Param<T> associatedParam, ModelConfig<T> config, EntityStateAspectHandlers provider, Model<?> mapsToSAC) {
		// instantiate
		DefaultModelState<T> mState = getFactory(associatedParam).instantiateModel(associatedParam, config, provider, mapsToSAC);
		
		// rules
		Optional.ofNullable(config.getRulesConfig())
			.map(rulesConfig->rulesEngineFactoryProducer.getFactory(config.getReferredClass()))
			.map(f->f.createRuntime(config.getRulesConfig()))
				.ifPresent(rt->mState.setRulesRuntime(rt));		
				
		mState.initSetup();
		return mState;
	}
	
	protected <T> DefaultListModelState<T> createCollectionModel(ListParam associatedParam, ModelConfig<List<T>> config, EntityStateAspectHandlers provider, DefaultListElemParamState.Creator<T> elemCreator) {
		// instantiate
		DefaultListModelState<T> mState = getFactory(associatedParam).instantiateColModel(associatedParam, config, provider, elemCreator);
		
		mState.initSetup();
		return mState;
	}
	
	/**
	 * Add in mapsTo model first, then in mapped
	 */
	protected <E> DefaultListElemParamState<E> createElemParam(EntityStateAspectHandlers provider, DefaultListModelState<E> parentModel, ParamConfig<E> mpConfig, String elemId) {
		/* if param is mapped then model must be mapped - in case of collection, its the model enclosing the collection param */
		Model<?> colParamParentModel = parentModel.getAssociatedParam().getParentModel();
		if(mpConfig.getMappingMode()==MapsTo.Mode.MappedAttached && !colParamParentModel.getConfig().isMapped() && !colParamParentModel.isRoot())
				throw new InvalidConfigException("Model class: "+colParamParentModel.getConfig().getReferredClass()+" must be mapped to load mapped param: "+mpConfig.findIfMapped().getPath());

		// instantiate
		final DefaultListElemParamState<E> mpState = getFactory(parentModel).instantiateColElemParam(provider, parentModel, mpConfig, elemId);
		
		mpState.initSetup();
		decorateParam(mpState);
		
		return mpState;
	}
	
	protected <P> DefaultParamState<P> createParam(EntityStateAspectHandlers provider, Model<?> parentModel, Model<?> mapsToSAC, ParamConfig<P> mpConfig) {
		logit.trace(()->"[createParam] paramConfig: "+mpConfig.getCode()+" in model: "+parentModel.getPath());
		
		final DefaultParamState<P> p;
		if(mpConfig.isMapped()) {
			p = createParamMapped(provider, parentModel, mapsToSAC, mpConfig.findIfMapped());
		}	
		else {
			p = createParamUnmapped(provider, parentModel, mapsToSAC, mpConfig);
		}
		
		p.initSetup();
		decorateParam(p);
		
		/* setting param values if applicable */
		createParamValues(p);
		
		return p;
	}
	
//	private <T> void decorateParam(DefaultParamState<T> created) {
//		if(created.getConfig().getContextParam()==null) //skip is not configured for RuntimeConfig
//			return; 
//
//		ParamConfig<StateContextEntity> pCofigContext = created.getConfig().getContextParam();
//		ModelConfig<StateContextEntity> mConfigContext = pCofigContext.getType().<StateContextEntity>findIfNested().getModel();
//
//		// create model config for StateContextEntity
//		ExecutionEntity.ExConfig<Object, StateContextEntity> exConfig = new ExecutionEntity.ExConfig<>(mConfigContext, null, null);
//		
//		// context model path
////		String ctxPath = created.getRootExecution().getRootCommand().buildAlias(Type.PlatformMarker) + //Constants.SEPARATOR_URI.code+ 
////							created.getPath();// +"/"+ created.getConfig().getContextParam().getCode();
////		Command ctxCmd = CommandBuilder.withUri(ctxPath).getCommand();
////		
////		String ctxParamPath = created.getPath();
//		Command ctxCmd = created.getRootExecution().getRootCommand();
//		
//		ExecutionEntity<Object, StateContextEntity> eStateCtx = new ExecutionEntity<>();
//		ExecutionEntity<Object, StateContextEntity>.ExParam exParamCtx = eStateCtx.new ExParamLinked(ctxCmd, created.getAspectHandlers(), exConfig, /*ctxParamPath,*/ created);
//		//exParamCtx.initSetup();
//		
//		ExecutionEntity<Object, StateContextEntity>.ExModel exModelCtx = exParamCtx.getRootExecution().unwrap(ExecutionEntity.ExModel.class);
//		//exModelCtx.initSetup();
//		
//		DefaultParamState<StateContextEntity> pCtx = buildParam(created.getAspectHandlers(), exModelCtx, exModelCtx.getConfig().getCoreParam(), null);
//		Model<StateContextEntity> mCtx = pCtx.findIfNested();
//		
//		created.setContextModel(mCtx);
//	}
	
	private <T> void decorateParam(DefaultParamState<T> created) {
		
	}
	
	private <P, V, C> DefaultParamState<P> createParamMapped(EntityStateAspectHandlers aspectHandlers, Model<?> parentModel, Model<?> mapsToSAC, MappedParamConfig<P, ?> mappedParamConfig) {
		if(mappedParamConfig.getMappingMode() == Mode.MappedAttached) {
			// find mapped param's state
			final Param<?> mapsToParam = findMapsToParam(mappedParamConfig, mapsToSAC);

			// handle transient
			if(mappedParamConfig.getPath().nature() == MapsTo.Nature.TransientColElem) {
				if(!mappedParamConfig.getType().isNested())
					throw new InvalidConfigException("Non nested transient params are not supported, but found for: "+mappedParamConfig.getCode());
				
				@SuppressWarnings("unchecked")
				ParamType.Nested<P> mpNmType = ((ParamType.Nested<P>)mappedParamConfig.getType());
				
				ModelConfig<P> mpNmConfig = mpNmType.getModel();
				MappedDefaultTransientParamState.Creator<P> creator = (associatedParam, transientMapsTo) -> buildModel(aspectHandlers, associatedParam, mpNmConfig, transientMapsTo);
				
				return new MappedDefaultTransientParamState<>(mapsToParam, parentModel, mappedParamConfig, aspectHandlers, creator);
			} 
			
			else if(mappedParamConfig.getPath().nature() != MapsTo.Nature.Default) {
				throw new UnsupportedOperationException("Transient behavior for: "+mappedParamConfig.getPath().nature()+" not yet implemented, found for: "+mappedParamConfig.getCode());
			}
			
			return getFactory(mappedParamConfig).instantiateParam(mapsToParam, parentModel, mappedParamConfig, aspectHandlers);
		}
		

		// detached nested
		
		ModelConfig<?> mapsToEnclosingModel = mappedParamConfig.getMapsToEnclosingModel();
		
		ExecutionEntity.ExConfig<V, C> exConfig = new ExecutionEntity.ExConfig<>((ModelConfig<C>)mapsToEnclosingModel, null, null);
		
		//==String mapsToParamAbsolutePath = parentModel.getAbsolutePath() +Constants.SEPARATOR_URI.code+ mappedParamConfig.getCode();
		String mapsToParamAbsolutePath = parentModel.getRootExecution().getRootCommand().buildAlias(Type.DomainAlias) 
											+Constants.SEPARATOR_URI.code+ mappedParamConfig.getCode();
		
		Command detachedRootCommand = CommandBuilder.withUri(mapsToParamAbsolutePath).getCommand();
		
		ExecutionEntity<V, C> eState = new ExecutionEntity<>();
		ExecutionEntity<V, C>.ExModel execModelSAC = eState.new ExParam(detachedRootCommand, aspectHandlers, exConfig).getRootExecution().unwrap(ExecutionEntity.ExModel.class);
		
		// mapsTo core param
		DefaultParamState<C> mapsToDetachedRootParam = buildParam(aspectHandlers, execModelSAC, execModelSAC.getConfig().getCoreParam(), null);
		Param<?> mapsToParam = mapsToDetachedRootParam.findParamByPath(MapsTo.DETACHED_SIMULATED_FIELD_NAME);
		
		return getFactory(mappedParamConfig).instantiateParam(mapsToParam, parentModel, mappedParamConfig, aspectHandlers);
	}

	private <P> DefaultParamState<P> createParamUnmapped(EntityStateAspectHandlers aspectHandlers, Model<?> parentModel, Model<?> mapsToSAC, ParamConfig<P> paramConfig) {
		return getFactory(paramConfig).instantiateParam(null, parentModel, paramConfig, aspectHandlers);
	}
	
	private <T, M> Param<M> findMapsToParam(ParamConfig<T> pConfig, Model<?> mapsToStateAndConfig) {
		if(pConfig==null || !pConfig.isMapped()) return null;
		
		return findMapsToParam(pConfig.findIfMapped(), mapsToStateAndConfig);
	}
	
	private <T, M> Param<M> findMapsToParam(MappedParamConfig<T, ?> mapped, Model<?> mapsToStateAndConfig) {

		String configuredPath = StringUtils.trimToNull(mapped.getPath().value());

		// handle root
		if(mapped.getMapsToEnclosingModel().isRoot()) {
			configuredPath = "/" + ((ExModelConfig)mapped.getMapsToEnclosingModel()).getCoreParam().getCode();
		}
		
		String resolvedPath = (configuredPath==null) ? mapped.getCode() : configuredPath;
		Param<M> mapsToParam = mapsToStateAndConfig.findParamByPath(resolvedPath);
			
		if(mapsToParam==null) 
			throw new InvalidConfigException("Param is mapped but no param found on mapped model. "
				+ "Finding by resolvedPath: "+resolvedPath+" on Mapped model: "+mapsToStateAndConfig.getPath()+" returned null. \n"
				+ "Mapped Param: "+mapped.getCode()+" with mapsTo: "+mapped.getPath().value()+" mapped model: "+mapsToStateAndConfig.getPath());
			
		return mapsToParam;	
	}
	
	private void createParamValues(Param<?> param) {
		Values values = param.getConfig().getValues();
		final List<ParamValue> result = buildValues(values, param, getGateway());
		
		if (result != null) {
			param.setValues(result);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<ParamValue> buildValues(Values values, Param<?> param, CommandExecutorGateway gateway) {
		List<ParamValue> result = null;
		if (values != null) {
			if (values.value() != EMPTY.class) {
				Source srcValues = ClassLoadUtils.newInstance(values.value());
				result = srcValues.getValues(param.getConfig().getCode());
			} else {
				String valuesUrl = values.url();
				Command cmd = CommandBuilder.withUri(valuesUrl).getCommand();
				cmd.setAction(Action._search);
				
				CommandMessage cmdMsg = new CommandMessage();
				cmdMsg.setCommand(cmd);
				
				MultiOutput multiOp = gateway.execute(cmdMsg);
				result = (List<ParamValue>) multiOp.getSingleResult();
			}
		}
		return result;
	}
	
}
