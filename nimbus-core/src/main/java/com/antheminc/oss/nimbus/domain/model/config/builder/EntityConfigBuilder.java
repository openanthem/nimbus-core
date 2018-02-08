package com.antheminc.oss.nimbus.domain.model.config.builder;

import java.lang.reflect.Field;

import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;

public interface EntityConfigBuilder {

	<T> ModelConfig<T> load(Class<T> clazz, EntityConfigVisitor visitedModels);

	<T> ModelConfig<T> buildModel(Class<T> clazz, EntityConfigVisitor visitedModels);

	<T> ParamConfig<?> buildParam(ModelConfig<T> mConfig, Field f, EntityConfigVisitor visitedModels);

}