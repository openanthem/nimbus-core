package com.antheminc.oss.nimbus.domain.model.state.builder;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultModelState;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultParamState;
import com.antheminc.oss.nimbus.domain.model.state.internal.ExecutionEntity;

public interface EntityStateBuilder {

	<V, C> ExecutionEntity<V, C>.ExModel buildExec(Command rootCommand, EntityStateAspectHandlers aspectHandlers,
			ExecutionEntity.ExConfig<V, C> exConfig, V viewEntityState, Param<C> coreParam);

	<V, C> ExecutionEntity<V, C>.ExModel buildExec(Command rootCommand, EntityStateAspectHandlers aspectHandlers,
			ExecutionEntity<V, C> eState, ExecutionEntity.ExConfig<V, C> exConfig);

	<T, P> DefaultParamState<P> buildParam(EntityStateAspectHandlers aspectHandlers, Model<T> mState,
			ParamConfig<P> mpConfig, Model<?> mapsToSAC);

	<T, P> DefaultModelState<T> buildModel(EntityStateAspectHandlers aspectHandlers,
			DefaultParamState<T> associatedParam, ModelConfig<T> mConfig, Model<?> mapsToSAC);

}