/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.MultiExecuteOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessGateway;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Mode;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig.MappedParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;
import com.anthem.oss.nimbus.core.domain.model.config.builder.AbstractEntityConfigBuilder.SimulatedCollectionParamEnclosingEntity;
import com.anthem.oss.nimbus.core.domain.model.config.internal.DefaultParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.internal.MappedDefaultParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.StateBuilderSupport;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultListElemParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultListModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultListParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultListElemParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultListModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultListParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.MappedDefaultParamState;
import com.anthem.oss.nimbus.core.rules.RulesEngineFactoryProducer;
import com.anthem.oss.nimbus.core.util.JustLogit;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
abstract public class AbstractStateBuilder {

	@Autowired RulesEngineFactoryProducer rulesEngineFactoryProducer;
	
	@Autowired
	@Qualifier("default.processGateway")
	ProcessGateway processGateway;
	
	protected JustLogit logit = new JustLogit(getClass());
	
	abstract public <T, P> DefaultParamState<P> buildParam(StateBuilderSupport provider, DefaultModelState<T> mState, ParamConfig<P> mpConfig, Model<?> mapsToSAC);

	protected <T> DefaultModelState<T> createModel(Param<T> associatedParam, ModelConfig<T> config, StateBuilderSupport provider, Model<?> mapsToSAC) {
		DefaultModelState<T> mState = associatedParam.isMapped() ? //(mapsToSAC!=null) ? 
				new MappedDefaultModelState<>(mapsToSAC, associatedParam, config, provider) : 
					new DefaultModelState<>(associatedParam, config, provider);
		
		// rules
		Optional.ofNullable(config.getRulesConfig())
			.map(rulesConfig->rulesEngineFactoryProducer.getFactory(config.getReferredClass()))
			.map(f->f.createRuntime(config.getRulesConfig()))
				.ifPresent(rt->mState.setRulesRuntime(rt));		
				
		mState.init();
		return mState;
	}
	
	protected <T> DefaultListModelState<T> createCollectionModel(ListParam associatedParam, ModelConfig<List<T>> config, StateBuilderSupport provider, DefaultListElemParamState.Creator<T> elemCreator) {
		DefaultListModelState<T> mState = (associatedParam.isMapped()) ? 
				new MappedDefaultListModelState<>(associatedParam.findIfMapped().getMapsTo().getType().findIfCollection().getModel(), associatedParam, config, provider, elemCreator) :
				new DefaultListModelState<>(associatedParam, config, provider, elemCreator);					
		
		mState.init();
		return mState;
	}
	
	/**
	 * Add in mapsTo model first, then in mapped
	 */
	protected <E> DefaultListElemParamState<E> createElemParam(StateBuilderSupport provider, DefaultListModelState<E> parentModel, ParamConfig<E> mpConfig, String elemId) {
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
		
		mpState.init();
		return mpState;
	}
	
	protected <P> DefaultParamState<P> createParam(StateBuilderSupport provider, DefaultModelState<?> parentModel, Model<?> mapsToSAC, ParamConfig<P> mpConfig) {
		final DefaultParamState<P> p;
		if(mpConfig.isMapped()) {
			p = createParamMapped(provider, parentModel, mapsToSAC, mpConfig.findIfMapped());
		}	
		else
			p = createParamUnmapped(provider, parentModel, mapsToSAC, mpConfig);
		
		p.init();
		
		/* setting param values if applicable */
		createParamValues(p.getConfig(), p.getPath());
		
		return p;
	}
	
	private <P, V, C> DefaultParamState<P> createParamMapped(StateBuilderSupport provider, DefaultModelState<?> parentModel, Model<?> mapsToSAC, MappedParamConfig<P, ?> mappedParamConfig) {
		if(mappedParamConfig.getMappingMode() == Mode.MappedAttached) {
			// find mapped param's state
			final Param<?> mapsToParam = findMapsToParam(mappedParamConfig, mapsToSAC);
			
			if(mappedParamConfig.getType().isCollection()) {
				return new MappedDefaultListParamState(mapsToParam.findIfCollection(), parentModel, mappedParamConfig, provider);
			}
				
			return new MappedDefaultParamState<>(mapsToParam, parentModel, mappedParamConfig, provider);
		}
		
		if(!mappedParamConfig.getMapsTo().getType().isNested()) {
			throw new UnsupportedOperationException("Mapped Detached ParamType.Field is not yet supported. Supported types are Nested & NestedCollection."
					+ " param: "+mappedParamConfig.getCode()+" in parent: "+parentModel.getPath());
		}
		
		// detached nested: find mapsTo model and create ExState.ExParam for it
		ExecutionEntity<V, C> eState = new ExecutionEntity<>();
		ModelConfig<?> mapsToEnclosingModel = mappedParamConfig.getMapsToEnclosingModel();
		
		ExecutionEntity.ExConfig<V, C> exConfig = new ExecutionEntity.ExConfig<>((ModelConfig<C>)mapsToEnclosingModel, null, null);
		
		Command cmdDelinked = CommandBuilder.withUri(parentModel.getRootExecution().getCommand().getAbsoluteUri()+Constants.SEPARATOR_URI.code+mappedParamConfig.getPath().value()).getCommand();
		ExecutionEntity<V, C>.ExModel execModelSAC = eState.new ExParam(cmdDelinked, provider, exConfig).getRootExecution().unwrap(ExecutionEntity.ExModel.class);
		
		// mapsTo core param
		DefaultParamState<C> mapsToDetachedRootParam = buildParam(provider, execModelSAC, execModelSAC.getConfig().getCoreParam(), null);
		Param<?> mapsToParam = mapsToDetachedRootParam.findParamByPath(MapsTo.DETACHED_SIMULATED_FIELD_NAME);
		
		if(mappedParamConfig.getType().isCollection()) {
			return new MappedDefaultListParamState(mapsToParam.findIfCollection(), parentModel, mappedParamConfig, provider);
		}
		return new MappedDefaultParamState<>(mapsToParam, parentModel, mappedParamConfig, provider);
	}

	private <P> DefaultParamState<P> createParamUnmapped(StateBuilderSupport provider, DefaultModelState<?> parentModel, Model<?> mapsToSAC, ParamConfig<P> paramConfig) {
		if(paramConfig.getType().isCollection())
			return new DefaultListParamState(parentModel, paramConfig, provider);
		
		return new DefaultParamState<>(parentModel, paramConfig, provider);
	}
	
	private <T, M> Param<M> findMapsToParam(ParamConfig<T> pConfig, Model<?> mapsToStateAndConfig) {
		if(pConfig==null || !pConfig.isMapped()) return null;
		
		return findMapsToParam(pConfig.findIfMapped(), mapsToStateAndConfig);
	}
	
	private <T, M> Param<M> findMapsToParam(MappedParamConfig<T, ?> mapped, Model<?> mapsToStateAndConfig) {
		//MappedParamConfig<T, M> mapped = (MappedParamConfig<T, M>)pConfig.findIfMapped();
		
		String configuredPath = /*(pConfig.getMapsTo()==null) ? null :*/ StringUtils.trimToNull(mapped.getPath().value());
		String resolvedPath = (configuredPath==null) ? mapped.getCode() : configuredPath;
		
		Param<M> mapsToParam = mapsToStateAndConfig.findParamByPath(resolvedPath);
			
		if(mapsToParam==null) 
			throw new InvalidConfigException("Param is mapped but no param found on mapped model. "
				+ "Finding by resolvedPath: "+resolvedPath+" on Mapped model: "+mapsToStateAndConfig.getPath()+" returned null. \n"
				+ "Mapped Param: "+mapped.getCode()+" with mapsTo: "+mapped.getPath().value()+" mapped model: "+mapsToStateAndConfig.getPath());
			
		return mapsToParam;	
	}
	
	private <P> void createParamValues(ParamConfig<P> currentParamConfig, String paramPath) {
		DefaultParamConfig<P> config = (DefaultParamConfig<P>) currentParamConfig;
		if(config.getValuesGetter() == null) {
			config.setValuesGetter(() -> valuesSupplier(currentParamConfig,paramPath));
		}
	}
	
	private <P> List<ParamValue> valuesSupplier(ParamConfig<P> currentParamConfig, String paramPath) {
		String valuesUrl = currentParamConfig.getValuesUrl();
		if(StringUtils.isBlank(valuesUrl) && currentParamConfig instanceof MappedDefaultParamConfig) {
			MappedDefaultParamConfig<?,?> attachedConfig = (MappedDefaultParamConfig) currentParamConfig;
			valuesUrl = attachedConfig.getMapsTo().getValuesUrl();
		}
		if(StringUtils.isBlank(valuesUrl)) 
			return null;
		
		String[] uriWithPayload = valuesUrl.split(Constants.CODE_VALUE_CONFIG_DELIMITER.code);
		Command cmd = CommandBuilder.withUri(Constants.PARAM_VALUES_URI_PREFIX.code+uriWithPayload[0]+Constants.PARAM_VALUES_URI_SUFFIX.code).getCommand();
		cmd.setAction(Action._lookup);
		cmd.templateBehaviors().add(Behavior.$execute);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		if(uriWithPayload.length > 1) {
			cmdMsg.setRawPayload(uriWithPayload[1]); // domain model lookup
		}
		else{
			cmdMsg.setRawPayload(paramPath); // static code value lookup
		}
		
		MultiExecuteOutput output = (MultiExecuteOutput) processGateway.startProcess(cmdMsg);
		return output.getSingleResult();
	
	}
}
