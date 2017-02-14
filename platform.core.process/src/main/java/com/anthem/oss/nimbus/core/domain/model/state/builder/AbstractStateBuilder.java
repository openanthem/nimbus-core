/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig.MappedParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.StateBuilderSupport;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultListElemParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultListModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultListParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionState;
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
	
	protected <T> DefaultListModelState<T> createCollectionModel(ListParam<T> associatedParam, ModelConfig<List<T>> config, StateBuilderSupport provider, DefaultListElemParamState.Creator<T> elemCreator) {
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
		if(mpConfig.isMapped())
			p = createParamMapped(provider, parentModel, mapsToSAC, mpConfig.findIfMapped());
		else
			p = createParamUnmapped(provider, parentModel, mapsToSAC, mpConfig);
		
		p.init();
		return p;
	}
	
	private <P, V, C> DefaultParamState<P> createParamMapped(StateBuilderSupport provider, DefaultModelState<?> parentModel, Model<?> mapsToSAC, MappedParamConfig<P, ?> mappedParamConfig) {
		if(mappedParamConfig.isLinked()) {
			// find mapped param's state
			final Param<?> mapsToParam = findMapsToParam(mappedParamConfig, mapsToSAC);
			
			if(mappedParamConfig.getType().isCollection()) {
				return new MappedDefaultListParamState(mapsToParam.findIfCollection(), parentModel, mappedParamConfig, provider);
			}
				
			return new MappedDefaultParamState<>(mapsToParam, parentModel, mappedParamConfig, provider);
		}
		
		// delinked: find mapsTo model and create ExState.ExParam for it
		ExecutionState<V, C> eState = new ExecutionState<>();
		ExecutionState.ExConfig<V, C> exConfig = new ExecutionState.ExConfig<>((ModelConfig<C>)mappedParamConfig.findIfDelinked().getMapsTo(), null, null);
		
		Command cmdDelinked = CommandBuilder.withUri(parentModel.getRootModel().getCommand().getAbsoluteUri()+Constants.SEPARATOR_URI.code+mappedParamConfig.getPath().value()).getCommand();
		ExecutionState<V, C>.ExModel execModelSAC = eState.new ExParam(cmdDelinked, provider, exConfig).getRootModel().unwrap(ExecutionState.ExModel.class);
		
		// mapsTo core param
		DefaultParamState<C> mapsToParam = buildParam(provider, execModelSAC, execModelSAC.getConfig().getCoreParam(), null);
		
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
}
