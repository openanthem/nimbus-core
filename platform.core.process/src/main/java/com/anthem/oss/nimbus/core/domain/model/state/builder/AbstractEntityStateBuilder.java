/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessGateway;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Mode;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig.MappedParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.anthem.oss.nimbus.core.domain.model.state.StateType;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultListElemParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultListModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultListParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity.ExModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultListElemParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultListModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultListParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.StateContextEntity;
import com.anthem.oss.nimbus.core.rules.RulesEngineFactoryProducer;
import com.anthem.oss.nimbus.core.util.JustLogit;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
abstract public class AbstractEntityStateBuilder {

	@Autowired RulesEngineFactoryProducer rulesEngineFactoryProducer;
	
	@Autowired
	@Qualifier("default.processGateway")
	ProcessGateway processGateway;
	
	protected JustLogit logit = new JustLogit(getClass());
	
	abstract public <T, P> DefaultModelState<T> buildModel(EntityStateAspectHandlers provider, DefaultParamState<T> associatedParam, ModelConfig<T> mConfig, Model<?> mapsToSAC);
	abstract public <T, P> DefaultParamState<P> buildParam(EntityStateAspectHandlers provider, Model<T> mState, ParamConfig<P> mpConfig, Model<?> mapsToSAC);
	abstract protected <P> StateType buildParamType(EntityStateAspectHandlers provider, DefaultParamState<P> associatedParam, Model<?> mapsToSAC);
	
	protected <T> DefaultModelState<T> createModel(Param<T> associatedParam, ModelConfig<T> config, EntityStateAspectHandlers provider, Model<?> mapsToSAC) {
		DefaultModelState<T> mState = associatedParam.isMapped() ? //(mapsToSAC!=null) ? 
				new MappedDefaultModelState<>(mapsToSAC, associatedParam, config, provider) : 
					new DefaultModelState<>(associatedParam, config, provider);
		
		// rules
		Optional.ofNullable(config.getRulesConfig())
			.map(rulesConfig->rulesEngineFactoryProducer.getFactory(config.getReferredClass()))
			.map(f->f.createRuntime(config.getRulesConfig()))
				.ifPresent(rt->mState.setRulesRuntime(rt));		
				
		mState.initSetup();
		return mState;
	}
	
	protected <T> DefaultListModelState<T> createCollectionModel(ListParam associatedParam, ModelConfig<List<T>> config, EntityStateAspectHandlers provider, DefaultListElemParamState.Creator<T> elemCreator) {
		DefaultListModelState<T> mState = (associatedParam.isMapped()) ? 
				new MappedDefaultListModelState<>(associatedParam.findIfMapped().getMapsTo().getType().findIfCollection().getModel(), associatedParam, config, provider, elemCreator) :
				new DefaultListModelState<>(associatedParam, config, provider, elemCreator);					
		
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
		
		final DefaultListElemParamState<E> mpState;
		if(parentModel.isMapped()) {
			mpState = new MappedDefaultListElemParamState<>(parentModel, mpConfig, provider, elemId);
			
		} else {
			mpState = new DefaultListElemParamState<>(parentModel, mpConfig, provider, elemId);
		}
		
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
	//	createParamValues(p.getConfig(), p.getPath());
		
		return p;
	}
	
	private <T> void decorateParam(DefaultParamState<T> created) {
		if(created.getConfig().getContextParam()==null) //skip is not configured for RuntimeConfig
			return; 

		ParamConfig<StateContextEntity> pCofigContext = created.getConfig().getContextParam();
		ModelConfig<StateContextEntity> mConfigContext = pCofigContext.getType().<StateContextEntity>findIfNested().getModel();

		// create model config for StateContextEntity
		ExecutionEntity.ExConfig<Object, StateContextEntity> exConfig = new ExecutionEntity.ExConfig<>(mConfigContext, null, null);
		
		// context model path
		String ctxPath = created.getRootExecution().getRootCommand().buildAlias(Type.PlatformMarker) + //Constants.SEPARATOR_URI.code+ 
							created.getPath() +"/"+ created.getConfig().getContextParam().getCode();
		Command ctxCmd = CommandBuilder.withUri(ctxPath).getCommand();
		
		ExecutionEntity<Object, StateContextEntity> eStateCtx = new ExecutionEntity<>();
		ExecutionEntity<Object, StateContextEntity>.ExParam exParamCtx = eStateCtx.new ExParam(ctxCmd, created.getAspectHandlers(), exConfig);
		//exParamCtx.initSetup();
		
		ExecutionEntity<Object, StateContextEntity>.ExModel exModelCtx = exParamCtx.getRootExecution().unwrap(ExecutionEntity.ExModel.class);
		//exModelCtx.initSetup();
		
		DefaultParamState<StateContextEntity> pCtx = buildParam(created.getAspectHandlers(), exModelCtx, exModelCtx.getConfig().getCoreParam(), null);
		Model<StateContextEntity> mCtx = pCtx.findIfNested();
		
		created.setContextModel(mCtx);
		
	}
	
	private <P, V, C> DefaultParamState<P> createParamMapped(EntityStateAspectHandlers aspectHandlers, Model<?> parentModel, Model<?> mapsToSAC, MappedParamConfig<P, ?> mappedParamConfig) {
		if(mappedParamConfig.getMappingMode() == Mode.MappedAttached) {
			// find mapped param's state
			final Param<?> mapsToParam = findMapsToParam(mappedParamConfig, mapsToSAC);
			
			if(mappedParamConfig.getType().isCollection()) {
				return new MappedDefaultListParamState(mapsToParam.findIfCollection(), parentModel, mappedParamConfig, aspectHandlers);
			}
				
			return new MappedDefaultParamState<>(mapsToParam, parentModel, mappedParamConfig, aspectHandlers);
		}
		
		if(!mappedParamConfig.getMapsTo().getType().isNested()) {
			throw new UnsupportedOperationException("Mapped Detached ParamType.Field is not yet supported. Supported types are Nested & NestedCollection."
					+ " param: "+mappedParamConfig.getCode()+" in parent: "+parentModel.getPath());
		}
		
		// detached nested: find mapsTo model and create ExState.ExParam for it
		
		ModelConfig<?> mapsToEnclosingModel = mappedParamConfig.getMapsToEnclosingModel();
		
		ExecutionEntity.ExConfig<V, C> exConfig = new ExecutionEntity.ExConfig<>((ModelConfig<C>)mapsToEnclosingModel, null, null);
		
		//==String mapsToParamAbsolutePath = parentModel.getAbsolutePath() +Constants.SEPARATOR_URI.code+ mappedParamConfig.getCode();
		String mapsToParamAbsolutePath = parentModel.getRootExecution().getRootCommand().buildAlias(Type.DomainAlias) +Constants.SEPARATOR_URI.code+ mappedParamConfig.getCode();
		Command detachedRootCommand = CommandBuilder.withUri(mapsToParamAbsolutePath).getCommand();
		
		ExecutionEntity<V, C> eState = new ExecutionEntity<>();
		ExecutionEntity<V, C>.ExModel execModelSAC = eState.new ExParam(detachedRootCommand, aspectHandlers, exConfig).getRootExecution().unwrap(ExecutionEntity.ExModel.class);
		
		// mapsTo core param
		DefaultParamState<C> mapsToDetachedRootParam = buildParam(aspectHandlers, execModelSAC, execModelSAC.getConfig().getCoreParam(), null);
		Param<?> mapsToParam = mapsToDetachedRootParam.findParamByPath(MapsTo.DETACHED_SIMULATED_FIELD_NAME);
		
		if(mappedParamConfig.getType().isCollection()) {
			return new MappedDefaultListParamState(mapsToParam.findIfCollection(), parentModel, mappedParamConfig, aspectHandlers);
		}
		return new MappedDefaultParamState<>(mapsToParam, parentModel, mappedParamConfig, aspectHandlers);
	}

	private <P> DefaultParamState<P> createParamUnmapped(EntityStateAspectHandlers aspectHandlers, Model<?> parentModel, Model<?> mapsToSAC, ParamConfig<P> paramConfig) {
		if(paramConfig.getType().isCollection())
			return new DefaultListParamState(parentModel, paramConfig, aspectHandlers);
		
		return new DefaultParamState<>(parentModel, paramConfig, aspectHandlers);
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
	
}
