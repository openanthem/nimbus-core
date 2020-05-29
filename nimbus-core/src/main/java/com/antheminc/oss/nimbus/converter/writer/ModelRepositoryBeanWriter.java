/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.converter.writer;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.converter.RowProcessable.RowProcessingHandler;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;
import com.antheminc.oss.nimbus.support.RefIdHolder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Sandeep Mantha
 * @author Tony Lopez
 *
 */
@RequiredArgsConstructor
@Getter
public class ModelRepositoryBeanWriter<T> implements RowProcessingHandler<T> {

	private final DomainConfigBuilder domainConfigBuilder;
	private final ModelRepositoryFactory modelRepositoryFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public void write(T bean) {
		ModelConfig<T> modelConfig = (ModelConfig<T>) getDomainConfigBuilder().getModel(bean.getClass());
		if (null == modelConfig) {
			throw new FrameworkRuntimeException("Unable to find model config for " + bean);
		}
		ModelRepository modelRepository = getModelRepositoryFactory().get(modelConfig.getRepo());
		RefIdHolder<T> refIdHolder = modelRepository._new(null, modelConfig, bean);
		T newState = refIdHolder.getState();
		modelRepository._save(modelConfig.getAlias(), newState);
	}

}
