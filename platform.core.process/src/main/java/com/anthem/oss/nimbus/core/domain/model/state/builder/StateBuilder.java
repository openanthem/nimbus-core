/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.StateBuilderSupport;
import com.anthem.oss.nimbus.core.domain.model.state.StateMeta;
import com.anthem.oss.nimbus.core.domain.model.state.StateType;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultListElemParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultListModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultModelState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.DefaultParamState;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;
import com.anthem.oss.nimbus.core.entity.process.ProcessFlow;

/**
 * @author Soham Chakravarti
 *
 */
@Component
public class StateBuilder extends AbstractStateBuilder {

	public <V, C> ExecutionEntity<V, C>.ExModel buildExec(Command cmd, StateBuilderSupport provider, ExecutionEntity<V, C> eState, StateMeta.View<V, C> viewMeta) {
		return buildExec(cmd, provider, eState, viewMeta.getExecutionConfig());
	}
	
	public <V, C> ExecutionEntity<V, C>.ExModel buildExec(Command cmd, StateBuilderSupport provider, ExecutionEntity<V, C> eState, ExecutionEntity.ExConfig<V, C> exConfig) {
		ExecutionEntity<V, C>.ExModel execModelSAC = eState.new ExParam(cmd, provider, exConfig).getRootExecution().unwrap(ExecutionEntity.ExModel.class);
		
		// core param sac
		DefaultParamState<C> coreParamSAC = buildParam(provider, execModelSAC, execModelSAC.getConfig().getCoreParam(), null);
		execModelSAC.templateParams().add(coreParamSAC);
		
		// view param sac
		if(exConfig.getView()!=null) {
			DefaultParamState<V> viewParamSAC = buildParam(provider, execModelSAC, execModelSAC.getConfig().getViewParam(), execModelSAC);
			execModelSAC.templateParams().add(viewParamSAC);
		}
		
		// flow param sac
		if(exConfig.getFlow()!=null) {
			DefaultParamState<ProcessFlow> flowParamSAC = buildParam(provider, execModelSAC, execModelSAC.getConfig().getFlowParam(), null);
			execModelSAC.templateParams().add(flowParamSAC);
		}
		
		execModelSAC.init();
		
		return execModelSAC;
	}

	@Override
	public <T, P> DefaultParamState<P> buildParam(StateBuilderSupport provider, DefaultModelState<T> mState, ParamConfig<P> mpConfig, Model<?> mapsToSAC) {
		final DefaultParamState<P> mpState = createParam(provider, mState, mapsToSAC, mpConfig);
		logit.debug(()->"[buildInternal] mpStatePath: "+ mpState.getPath());
		
		//handle param type
		StateType type = buildParamType(provider, mpConfig, mState, mpState, mapsToSAC);
		mpState.setType(type);
		
		return mpState;
	}
	
	public <T, P> DefaultModelState<T> buildModel(StateBuilderSupport provider, DefaultParamState<T> parentState, ModelConfig<T> mConfig, DefaultModelState<?> mapsToSAC) {
		return buildModelInternal(provider, parentState, mConfig, mapsToSAC);
	}
	

	protected <T, P> DefaultModelState<T> buildModelInternal(StateBuilderSupport provider, DefaultParamState<T> associatedParam, ModelConfig<T> mConfig, Model<?> mapsToSAC) {
		
		if(mConfig==null) return null;

		/* if model & param are mapped, then  mapsToSAC must not be null */
		if(mConfig.isMapped() && mapsToSAC==null) 
			throw new InvalidConfigException("Model class: "+mConfig.getReferredClass()+" is mapped: "+mConfig.findIfMapped().getMapsTo().getReferredClass()
						+" but mapsToSAC is not supplied for param: "+associatedParam.getPath()+". Was this model's config loaded first as part of core?");
		
		DefaultModelState<T> mState = createModel(associatedParam, mConfig, provider, mapsToSAC); //(provider, parentState, mConfig, mGet, mSet, mapsToSAC);
		
		if(mConfig.getParams()==null) return mState;
		
		/* iterate through config params and create state instances in the same order */
		for(ParamConfig<?> mpConfigRawType : mConfig.getParams()) {
			@SuppressWarnings("unchecked")
			final ParamConfig<P> mpConfig = (ParamConfig<P>)mpConfigRawType;
			
			final DefaultParamState<P> mpState = buildParam(provider, mState, mpConfig, mapsToSAC);
			 
			/* add param state to model state in same order */
			mState.templateParams().add(mpState);
		}
		
		return mState;
	}
	
	public <E> DefaultListElemParamState<E> buildElemParam(StateBuilderSupport provider, DefaultListModelState<E> mState, ParamConfig<E> mpConfig, String elemId) {
		final DefaultListElemParamState<E> mpState = createElemParam(provider, mState, mpConfig, elemId);
		logit.debug(()->"[buildInternal] mpStatePath: "+ mpState.getPath());
		

		//handle param type
		StateType type = buildParamType(provider, mpConfig, mState, mpState, Optional.ofNullable(mState.findIfMapped()).map(m->m.getMapsTo()).orElse(null));
		mpState.setType(type);
		
		return mpState;
	}
	
	protected <P> StateType buildParamType(StateBuilderSupport provider, ParamConfig<P> mpConfig, DefaultModelState<?> mState, DefaultParamState<P> associatedParam, Model<?> mapsToSAC) {
		
		if(mpConfig.getType().isCollection()) {
			ParamType.NestedCollection<P> nmcType = mpConfig.getType().findIfCollection();
			ModelConfig<List<P>> nmConfig = nmcType.getModel();
			
			DefaultListElemParamState.Creator<P> elemCreator = (colModelState, elemId) -> buildElemParam(provider, colModelState, colModelState.getElemConfig(), elemId);
			DefaultListModelState<P> nmcState = createCollectionModel(associatedParam.findIfCollection(), nmConfig, provider, elemCreator); 
			
			StateType.NestedCollection<P> nctSAC = new StateType.NestedCollection<>(nmcType, nmcState);
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
			DefaultModelState<P> nmState = buildModelInternal(provider, associatedParam, mpNmConfig, mpNmMapsToSAC);
			StateType.Nested<P> ntState = new StateType.Nested<>(mpNmType, nmState);
			return ntState;
			
		} else {
			
			return new StateType(mpConfig.getType());
		}
	}
	
}
