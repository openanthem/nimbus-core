/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import java.util.List;
import java.util.Optional;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
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
public class EntityStateBuilder extends AbstractEntityStateBuilder {

	public EntityStateBuilder(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	private interface ParamStateLoaderCallback<V, C> {
		
		Param<C> loadCore(ExecutionEntity<V, C>.ExModel execModel);
		
		Param<V> loadView(ExecutionEntity<V, C>.ExModel execModel);
	}
	
	public <V, C> ExecutionEntity<V, C>.ExModel buildExec(Command rootCommand, EntityStateAspectHandlers aspectHandlers, ExecutionEntity.ExConfig<V, C> exConfig, V viewEntityState, Param<C> coreParam) {
		// create entity state shell
		C coreEntityState = coreParam.getState();
		ExecutionEntity<V, C> eState = (ExecutionEntity<V, C>)ExecutionEntity.resolveAndInstantiate(viewEntityState, coreEntityState);
		
		return buildExec(rootCommand, aspectHandlers, eState, exConfig, new ParamStateLoaderCallback<V, C>() {
			@Override
			public Param<C> loadCore(ExecutionEntity<V, C>.ExModel execModel) {
				return coreParam;
			}
			
			@Override
			public Param<V> loadView(ExecutionEntity<V, C>.ExModel execModel) {
				return buildParam(aspectHandlers, execModel, execModel.getConfig().getViewParam(), execModel);
			}
		});
	}
	
	public <V, C> ExecutionEntity<V, C>.ExModel buildExec(Command rootCommand, EntityStateAspectHandlers aspectHandlers, ExecutionEntity<V, C> eState, ExecutionEntity.ExConfig<V, C> exConfig) {
		return buildExec(rootCommand, aspectHandlers, eState, exConfig, new ParamStateLoaderCallback<V, C>() {
			@Override
			public Param<C> loadCore(ExecutionEntity<V, C>.ExModel execModel) {
				return buildParam(aspectHandlers, execModel, execModel.getConfig().getCoreParam(), null);
			}
			
			@Override
			public Param<V> loadView(ExecutionEntity<V, C>.ExModel execModel) {
				return buildParam(aspectHandlers, execModel, execModel.getConfig().getViewParam(), execModel);
			}
		});
	}
	
	private <V, C> ExecutionEntity<V, C>.ExModel buildExec(Command rootCommand, EntityStateAspectHandlers aspectHandlers, ExecutionEntity<V, C> eState, ExecutionEntity.ExConfig<V, C> exConfig, ParamStateLoaderCallback<V, C> cb) {
		ExecutionEntity<V, C>.ExModel execModel = eState.new ExParam(rootCommand, aspectHandlers, exConfig).getRootExecution().unwrap(ExecutionEntity.ExModel.class);
		
		// add core param
		Param<C> coreParam = cb.loadCore(execModel);
		execModel.templateParams().add(coreParam);
		
		// add view param
		if(exConfig.getView()!=null) {
			Param<V> viewParam = cb.loadView(execModel);
			execModel.templateParams().add(viewParam);
		}
		
		// add flow param
		if(exConfig.getFlow()!=null) {
			DefaultParamState<ProcessFlow> flowParamSAC = buildParam(execModel.getAspectHandlers(), execModel, execModel.getConfig().getFlowParam(), null);
			execModel.templateParams().add(flowParamSAC);
		}
		
		execModel.initSetup();
		return execModel;
	}

	@Override
	public <T, P> DefaultParamState<P> buildParam(EntityStateAspectHandlers aspectHandlers, Model<T> mState, ParamConfig<P> mpConfig, Model<?> mapsToSAC) {
		final DefaultParamState<P> mpState = createParam(aspectHandlers, mState, mapsToSAC, mpConfig);
		logit.debug(()->"[buildInternal] mpStatePath: "+ mpState.getPath());
		
		//handle param type
		StateType type = buildParamType(aspectHandlers, mpState, mapsToSAC);
		mpState.setType(type);
		
		return mpState;
	}
	
	@Override
	public <T, P> DefaultModelState<T> buildModel(EntityStateAspectHandlers aspectHandlers, DefaultParamState<T> associatedParam, ModelConfig<T> mConfig, Model<?> mapsToSAC) {
		if(mConfig==null) return null;

		/* if model & param are mapped, then  mapsToSAC must not be null */
		if(mConfig.isMapped() && mapsToSAC==null) 
			throw new InvalidConfigException("Model class: "+mConfig.getReferredClass()+" is mapped: "+mConfig.findIfMapped().getMapsTo().getReferredClass()
						+" but mapsToSAC is not supplied for param: "+associatedParam.getPath()+". Was this model's config loaded first as part of core?");
		
		DefaultModelState<T> mState = createModel(associatedParam, mConfig, aspectHandlers, mapsToSAC); 
		
		if(mConfig.getParams()==null) return mState;
		
		/* iterate through config params and create state instances in the same order */
		for(ParamConfig<?> mpConfigRawType : mConfig.getParams()) {
			@SuppressWarnings("unchecked")
			final ParamConfig<P> mpConfig = (ParamConfig<P>)mpConfigRawType;
			
			final DefaultParamState<P> mpState = buildParam(aspectHandlers, mState, mpConfig, mapsToSAC);
			 
			/* add param state to model state in same order */
			mState.templateParams().add(mpState);
		}
		
		return mState;
	}
	
	private <E> DefaultListElemParamState<E> buildElemParam(EntityStateAspectHandlers aspectHandlers, DefaultListModelState<E> mState, ParamConfig<E> mpConfig, String elemId) {
		final DefaultListElemParamState<E> mpState = createElemParam(aspectHandlers, mState, mpConfig, elemId);
		logit.debug(()->"[buildInternal] mpStatePath: "+ mpState.getPath());
		

		//handle param type
		StateType type = buildParamType(aspectHandlers, mpState, Optional.ofNullable(mState.findIfMapped()).map(m->m.getMapsTo()).orElse(null));
		mpState.setType(type);
		
		return mpState;
	}
	
	@Override
	protected <P> StateType buildParamType(EntityStateAspectHandlers aspectHandlers, DefaultParamState<P> associatedParam, Model<?> mapsToSAC) {
		
		if(associatedParam.getConfig().getType().isCollection()) {
			ParamType.NestedCollection<P> nmcType = associatedParam.getConfig().getType().findIfCollection();
			ModelConfig<List<P>> nmConfig = nmcType.getModel();
			
			DefaultListElemParamState.Creator<P> elemCreator = (colModelState, elemId) -> buildElemParam(aspectHandlers, colModelState, colModelState.getElemConfig(), elemId);
			DefaultListModelState<P> nmcState = createCollectionModel(associatedParam.findIfCollection(), nmConfig, aspectHandlers, elemCreator); 
			
			StateType.NestedCollection<P> nctSAC = new StateType.NestedCollection<>(nmcType, nmcState);
			return nctSAC;
			
		} else if(associatedParam.getConfig().getType().isNested()) {
			//handle nested model
			@SuppressWarnings("unchecked")
			ParamType.Nested<P> mpNmType = ((ParamType.Nested<P>)associatedParam.getConfig().getType());
			
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
			DefaultModelState<P> nmState = buildModel(aspectHandlers, associatedParam, mpNmConfig, mpNmMapsToSAC);
			StateType.Nested<P> ntState = new StateType.Nested<>(mpNmType, nmState);
			return ntState;
			
		} else {
			
			return new StateType(associatedParam.getConfig().getType());
		}
	}
	
}
