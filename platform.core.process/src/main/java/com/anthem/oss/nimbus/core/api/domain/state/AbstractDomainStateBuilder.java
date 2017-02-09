/**
 * 
 */
package com.anthem.oss.nimbus.core.api.domain.state;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.command.CommandBuilder;
import com.anthem.nimbus.platform.spec.model.dsl.Constants;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainListElemParamState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainListModelState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainListParamState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainModelState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainParamState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ExecutionState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.MDomainListElemParamState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.MDomainListModelState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.MDomainListParamState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.MDomainModelState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.MDomainParamState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState.ListParam;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState.Model;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState.Param;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig.MappedParamConfig;
import com.anthem.nimbus.platform.spec.model.exception.InvalidConfigException;
import com.anthem.nimbus.platform.spec.model.util.JustLogit;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
abstract public class AbstractDomainStateBuilder {

	protected JustLogit logit = new JustLogit(getClass());
	
	abstract public <T, P> DomainParamState<P> buildParam(StateAndConfigSupportProvider provider, DomainModelState<T> mState, ParamConfig<P> mpConfig, Model<?> mapsToSAC);

	protected <T> DomainModelState<T> createModel(Param<T> associatedParam, ModelConfig<T> config, StateAndConfigSupportProvider provider, Model<?> mapsToSAC) {
		DomainModelState<T> mState = associatedParam.isMapped() ? //(mapsToSAC!=null) ? 
				new MDomainModelState<>(mapsToSAC, associatedParam, config, provider) : 
					new DomainModelState<>(associatedParam, config, provider);
		
		mState.init();
		return mState;
	}
	
	protected <T> DomainListModelState<T> createCollectionModel(ListParam<T> associatedParam, ModelConfig<List<T>> config, StateAndConfigSupportProvider provider, DomainListElemParamState.Creator<T> elemCreator) {
		DomainListModelState<T> mState = (associatedParam.isMapped()) ? 
				new MDomainListModelState<>(associatedParam.findIfMapped().getMapsTo().getType().findIfCollection().getModel(), associatedParam, config, provider, elemCreator) :
				new DomainListModelState<>(associatedParam, config, provider, elemCreator);					
		
		mState.init();
		return mState;
	}
	
	/**
	 * Add in mapsTo model first, then in mapped
	 */
	protected <E> DomainListElemParamState<E> createElemParam(StateAndConfigSupportProvider provider, DomainListModelState<E> parentModel, ParamConfig<E> mpConfig, String elemId) {
		/* if param is mapped then model must be mapped - in case of collection, its the model enclosing the collection param */
		Model<?> colParamParentModel = parentModel.getAssociatedParam().getParentModel();
		if(mpConfig.getMappingMode()==MapsTo.Mode.MappedAttached && !colParamParentModel.getConfig().isMapped() && !colParamParentModel.isRoot())
				throw new InvalidConfigException("Model class: "+colParamParentModel.getConfig().getReferredClass()+" must be mapped to load mapped param: "+mpConfig.findIfMapped().getPath());
		
		final DomainListElemParamState<E> mpState;
		if(parentModel.isMapped()) {
			mpState = new MDomainListElemParamState<>(parentModel, mpConfig, provider, elemId);
			
		} else {
			mpState = new DomainListElemParamState<>(parentModel, mpConfig, provider, elemId);
		}
		
		mpState.init();
		return mpState;
	}
	
	protected <P> DomainParamState<P> createParam(StateAndConfigSupportProvider provider, DomainModelState<?> parentModel, Model<?> mapsToSAC, ParamConfig<P> mpConfig) {
		final DomainParamState<P> p;
		if(mpConfig.isMapped())
			p = createParamMapped(provider, parentModel, mapsToSAC, mpConfig.findIfMapped());
		else
			p = createParamUnmapped(provider, parentModel, mapsToSAC, mpConfig);
		
		p.init();
		return p;
	}
	
	private <P, V, C> DomainParamState<P> createParamMapped(StateAndConfigSupportProvider provider, DomainModelState<?> parentModel, Model<?> mapsToSAC, MappedParamConfig<P, ?> mappedParamConfig) {
		if(mappedParamConfig.isLinked()) {
			// find mapped param's state
			final Param<?> mapsToParam = findMapsToParam(mappedParamConfig, mapsToSAC);
			
			if(mappedParamConfig.getType().isCollection()) {
				return new MDomainListParamState(mapsToParam.findIfCollection(), parentModel, mappedParamConfig, provider);
			}
				
			return new MDomainParamState<>(mapsToParam, parentModel, mappedParamConfig, provider);
		}
		
		// delinked: find mapsTo model and create ExState.ExParam for it
		ExecutionState<V, C> eState = new ExecutionState<>();
		ExecutionState.Config<V, C> exConfig = new ExecutionState.Config<>((ModelConfig<C>)mappedParamConfig.findIfDelinked().getMapsTo(), null, null);
		
		Command cmdDelinked = CommandBuilder.withUri(parentModel.getRootModel().getCommand().getAbsoluteUri()+Constants.SEPARATOR_URI.code+mappedParamConfig.getPath().value()).getCommand();
		ExecutionState<V, C>.ExModel execModelSAC = eState.new ExParam(cmdDelinked, provider, exConfig).getRootModel().unwrap(ExecutionState.ExModel.class);
		
		// mapsTo core param
		DomainParamState<C> mapsToParam = buildParam(provider, execModelSAC, execModelSAC.getConfig().getCoreParam(), null);
		
		return new MDomainParamState<>(mapsToParam, parentModel, mappedParamConfig, provider);
	}

	private <P> DomainParamState<P> createParamUnmapped(StateAndConfigSupportProvider provider, DomainModelState<?> parentModel, Model<?> mapsToSAC, ParamConfig<P> paramConfig) {
		if(paramConfig.getType().isCollection())
			return new DomainListParamState(parentModel, paramConfig, provider);
		
		return new DomainParamState<>(parentModel, paramConfig, provider);
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
