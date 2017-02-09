/**
 * 
 */
package com.anthem.oss.nimbus.core.api.domain.state;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainListElemParamState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainListModelState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainModelState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainParamState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ExecutionState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.FlowState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState.Model;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfigMeta;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainStateType;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamType;
import com.anthem.nimbus.platform.spec.model.exception.InvalidConfigException;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;

/**
 * @author Soham Chakravarti
 *
 */
@Component
public class DomainStateBuilder extends AbstractDomainStateBuilder {

	public <V, C> ExecutionState<V, C>.ExModel buildExec(Command cmd, StateAndConfigSupportProvider provider, ExecutionState<V, C> eState, StateAndConfigMeta.View<V, C> viewMeta) {
		return buildExec(cmd, provider, eState, viewMeta.getExecutionConfig());
	}
	
	public <V, C> ExecutionState<V, C>.ExModel buildExec(Command cmd, StateAndConfigSupportProvider provider, ExecutionState<V, C> eState, ExecutionState.Config<V, C> exConfig) {
		ExecutionState<V, C>.ExModel execModelSAC = eState.new ExParam(cmd, provider, exConfig).getRootModel().unwrap(ExecutionState.ExModel.class);
		
		// core param sac
		DomainParamState<C> coreParamSAC = buildParam(provider, execModelSAC, execModelSAC.getConfig().getCoreParam(), null);
		execModelSAC.templateParams().add(coreParamSAC);
		
		// view param sac
		if(exConfig.getView()!=null) {
			DomainParamState<V> viewParamSAC = buildParam(provider, execModelSAC, execModelSAC.getConfig().getViewParam(), execModelSAC);
			execModelSAC.templateParams().add(viewParamSAC);
		}
		
		// flow param sac
		if(exConfig.getFlow()!=null) {
			DomainParamState<FlowState> flowParamSAC = buildParam(provider, execModelSAC, execModelSAC.getConfig().getFlowParam(), null);
			execModelSAC.templateParams().add(flowParamSAC);
		}
		
		return execModelSAC;
	}

	@Override
	public <T, P> DomainParamState<P> buildParam(StateAndConfigSupportProvider provider, DomainModelState<T> mState, ParamConfig<P> mpConfig, Model<?> mapsToSAC) {
		final DomainParamState<P> mpState = createParam(provider, mState, mapsToSAC, mpConfig);
		logit.debug(()->"[buildInternal] mpStatePath: "+ mpState.getPath());
		
		//handle param type
		DomainStateType type = buildParamType(provider, mpConfig, mState, mpState, mapsToSAC);
		mpState.setType(type);
		
		return mpState;
	}
	
	public <T, P> DomainModelState<T> buildModel(StateAndConfigSupportProvider provider, DomainParamState<T> parentState, ModelConfig<T> mConfig, DomainModelState<?> mapsToSAC) {
		return buildModelInternal(provider, parentState, mConfig, mapsToSAC);
	}
	

	protected <T, P> DomainModelState<T> buildModelInternal(StateAndConfigSupportProvider provider, DomainParamState<T> associatedParam, ModelConfig<T> mConfig, Model<?> mapsToSAC) {
		
		if(mConfig==null) return null;

		/* if model & param are mapped, then  mapsToSAC must not be null */
		if(mConfig.isMapped() && mapsToSAC==null) 
			throw new InvalidConfigException("Model class: "+mConfig.getReferredClass()+" is mapped: "+mConfig.findIfMapped().getMapsTo().getReferredClass()
						+" but mapsToSAC is not supplied for param: "+associatedParam.getPath()+". Was this model's config loaded first as part of core?");
		
		DomainModelState<T> mState = createModel(associatedParam, mConfig, provider, mapsToSAC); //(provider, parentState, mConfig, mGet, mSet, mapsToSAC);
		
		if(mConfig.getParams()==null) return mState;
		
		/* iterate through config params and create state instances in the same order */
		for(ParamConfig<?> mpConfigRawType : mConfig.getParams()) {
			@SuppressWarnings("unchecked")
			final ParamConfig<P> mpConfig = (ParamConfig<P>)mpConfigRawType;
			
			final DomainParamState<P> mpState = buildParam(provider, mState, mpConfig, mapsToSAC);
			 
			/* add param state to model state in same order */
			mState.templateParams().add(mpState);
		}
		
		return mState;
	}
	
	public <E> DomainListElemParamState<E> buildElemParam(StateAndConfigSupportProvider provider, DomainListModelState<E> mState, ParamConfig<E> mpConfig, String elemId) {
		final DomainListElemParamState<E> mpState = createElemParam(provider, mState, mpConfig, elemId);
		logit.debug(()->"[buildInternal] mpStatePath: "+ mpState.getPath());
		

		//handle param type
		DomainStateType type = buildParamType(provider, mpConfig, mState, mpState, Optional.ofNullable(mState.findIfMapped()).map(m->m.getMapsTo()).orElse(null));
		mpState.setType(type);
		
		return mpState;
	}
	
	protected <P> DomainStateType buildParamType(StateAndConfigSupportProvider provider, ParamConfig<P> mpConfig, DomainModelState<?> mState, DomainParamState<P> associatedParam, Model<?> mapsToSAC) {
		
		if(mpConfig.getType().isCollection()) {
			ParamType.NestedCollection<P> nmcType = mpConfig.getType().findIfCollection();
			ModelConfig<List<P>> nmConfig = nmcType.getModel();
			
			DomainListElemParamState.Creator<P> elemCreator = (colModelState, elemId) -> buildElemParam(provider, colModelState, colModelState.getElemConfig(), elemId);
			DomainListModelState<P> nmcState = createCollectionModel(associatedParam.findIfCollection(), nmConfig, provider, elemCreator); 
			
			DomainStateType.NestedCollection<P> nctSAC = new DomainStateType.NestedCollection<>(nmcType, nmcState);
			return nctSAC;
			
		} else if(mpConfig.getType().isNested()) {
			//handle nested model
			@SuppressWarnings("unchecked")
			ParamType.Nested<P> mpNmType = ((ParamType.Nested<P>)mpConfig.getType());
			
			ModelConfig<P> mpNmConfig = mpNmType.getModel();
			
			/* determine mapsTo model SAC for param's nested model */
			final Model<?> mpNmMapsToSAC;
			
			if(!associatedParam.isMapped()) {
				mpNmMapsToSAC = mapsToSAC;
			} else if(associatedParam.getConfig().getType().isNested()) {
				mpNmMapsToSAC = associatedParam.findIfMapped().getMapsTo().getType().findIfNested().getModel();
			} else { // leaf
				mpNmMapsToSAC = associatedParam.findIfMapped().getMapsTo().getParentModel();
			}
			
			
			/* create nested model SAC */
			DomainModelState<P> nmState = buildModelInternal(provider, associatedParam, mpNmConfig, mpNmMapsToSAC);
			DomainStateType.Nested<P> ntState = new DomainStateType.Nested<>(mpNmType, nmState);
			return ntState;
			
		} else {
			
			return new DomainStateType(mpConfig.getType());
		}
	}
	
}
