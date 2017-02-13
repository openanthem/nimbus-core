/**
 * 
 */
package com.anthem.oss.nimbus.core.api.domain.state;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.anthem.nimbus.platform.spec.model.util.JustLogit;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.anthem.oss.nimbus.core.domain.Command;
import com.anthem.oss.nimbus.core.domain.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.Constants;
import com.anthem.oss.nimbus.core.domain.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.MapsTo;
import com.anthem.oss.nimbus.core.domain.model.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.ParamConfig.MappedParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionState;
import com.anthem.oss.nimbus.core.domain.model.state.DefaultListElemParamState;
import com.anthem.oss.nimbus.core.domain.model.state.DefaultListModelState;
import com.anthem.oss.nimbus.core.domain.model.state.DefaultListParamState;
import com.anthem.oss.nimbus.core.domain.model.state.MappedDefaultListElemParamState;
import com.anthem.oss.nimbus.core.domain.model.state.MappedDefaultListModelState;
import com.anthem.oss.nimbus.core.domain.model.state.MappedDefaultListParamState;
import com.anthem.oss.nimbus.core.domain.model.state.MappedDefaultModelState;
import com.anthem.oss.nimbus.core.domain.model.state.MappedDefaultParamState;
import com.anthem.oss.nimbus.core.domain.model.state.DefaultModelState;
import com.anthem.oss.nimbus.core.domain.model.state.DefaultParamState;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.Param;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
abstract public class AbstractDomainStateBuilder {

	protected JustLogit logit = new JustLogit(getClass());
	
	abstract public <T, P> DefaultParamState<P> buildParam(StateAndConfigSupportProvider provider, DefaultModelState<T> mState, ParamConfig<P> mpConfig, Model<?> mapsToSAC);

	protected <T> DefaultModelState<T> createModel(Param<T> associatedParam, ModelConfig<T> config, StateAndConfigSupportProvider provider, Model<?> mapsToSAC) {
		DefaultModelState<T> mState = associatedParam.isMapped() ? //(mapsToSAC!=null) ? 
				new MappedDefaultModelState<>(mapsToSAC, associatedParam, config, provider) : 
					new DefaultModelState<>(associatedParam, config, provider);
		
		mState.init();
		return mState;
	}
	
	protected <T> DefaultListModelState<T> createCollectionModel(ListParam<T> associatedParam, ModelConfig<List<T>> config, StateAndConfigSupportProvider provider, DefaultListElemParamState.Creator<T> elemCreator) {
		DefaultListModelState<T> mState = (associatedParam.isMapped()) ? 
				new MappedDefaultListModelState<>(associatedParam.findIfMapped().getMapsTo().getType().findIfCollection().getModel(), associatedParam, config, provider, elemCreator) :
				new DefaultListModelState<>(associatedParam, config, provider, elemCreator);					
		
		mState.init();
		return mState;
	}
	
	/**
	 * Add in mapsTo model first, then in mapped
	 */
	protected <E> DefaultListElemParamState<E> createElemParam(StateAndConfigSupportProvider provider, DefaultListModelState<E> parentModel, ParamConfig<E> mpConfig, String elemId) {
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
	
	protected <P> DefaultParamState<P> createParam(StateAndConfigSupportProvider provider, DefaultModelState<?> parentModel, Model<?> mapsToSAC, ParamConfig<P> mpConfig) {
		final DefaultParamState<P> p;
		if(mpConfig.isMapped())
			p = createParamMapped(provider, parentModel, mapsToSAC, mpConfig.findIfMapped());
		else
			p = createParamUnmapped(provider, parentModel, mapsToSAC, mpConfig);
		
		p.init();
		return p;
	}
	
	private <P, V, C> DefaultParamState<P> createParamMapped(StateAndConfigSupportProvider provider, DefaultModelState<?> parentModel, Model<?> mapsToSAC, MappedParamConfig<P, ?> mappedParamConfig) {
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
		ExecutionState.Config<V, C> exConfig = new ExecutionState.Config<>((ModelConfig<C>)mappedParamConfig.findIfDelinked().getMapsTo(), null, null);
		
		Command cmdDelinked = CommandBuilder.withUri(parentModel.getRootModel().getCommand().getAbsoluteUri()+Constants.SEPARATOR_URI.code+mappedParamConfig.getPath().value()).getCommand();
		ExecutionState<V, C>.ExModel execModelSAC = eState.new ExParam(cmdDelinked, provider, exConfig).getRootModel().unwrap(ExecutionState.ExModel.class);
		
		// mapsTo core param
		DefaultParamState<C> mapsToParam = buildParam(provider, execModelSAC, execModelSAC.getConfig().getCoreParam(), null);
		
		return new MappedDefaultParamState<>(mapsToParam, parentModel, mappedParamConfig, provider);
	}

	private <P> DefaultParamState<P> createParamUnmapped(StateAndConfigSupportProvider provider, DefaultModelState<?> parentModel, Model<?> mapsToSAC, ParamConfig<P> paramConfig) {
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
