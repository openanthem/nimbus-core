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
package com.antheminc.oss.nimbus.domain.model.config.internal;

import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig.MappedTransientParamConfig;

/**
 * @author Soham Chakravarti
 *
 */
public class MappedDefaultTransientParamConfig<P, M> extends MappedDefaultParamConfig<P, M> implements MappedTransientParamConfig<P, M> {

	private static final long serialVersionUID = 1L;
	
	private final ParamConfig<P> simulatedMappedDetached;

	public MappedDefaultTransientParamConfig(ParamConfig<P> simulatedMappedDetached, String code, ModelConfig<?> mapsToEnclosingModel, ParamConfig<M> mapsTo, Path path) {
		super(code, mapsToEnclosingModel, mapsTo, path);
		this.simulatedMappedDetached = simulatedMappedDetached;
	}
	
	public MappedDefaultTransientParamConfig(ParamConfig<P> simulatedMappedDetached, String code, String beanName, ModelConfig<?> mapsToEnclosingModel, ParamConfig<M> mapsToConfig, Path path) {
		super(code, beanName, mapsToEnclosingModel, mapsToConfig, path);
		this.simulatedMappedDetached = simulatedMappedDetached;
	}
	
	public MappedDefaultTransientParamConfig(ParamConfig<P> simulatedMappedDetached, String code, String beanName, ModelConfig<?> mapsToEnclosingModel, ParamConfig<M> mapsToConfig, Path path, String id) {
		super(code, beanName, mapsToEnclosingModel, mapsToConfig, path, id);
		this.simulatedMappedDetached = simulatedMappedDetached;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public MappedParamConfig<P, M> getSimulatedDetached() {
		return MappedParamConfig.class.cast(simulatedMappedDetached);
	}
}
