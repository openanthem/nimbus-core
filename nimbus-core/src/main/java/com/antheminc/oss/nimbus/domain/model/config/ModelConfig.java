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
package com.antheminc.oss.nimbus.domain.model.config;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.support.pojo.CollectionsTemplate;

/**
 * @author Soham Chakravarti
 *
 */
public interface ModelConfig<T> extends EntityConfig<T> {
	
	public String getAlias();
	
	public String getRepoAlias();
	
	public String getDomainLifecycle();
	
	public Repo getRepo();
	
	public boolean isRemote();

	//@JsonIgnore
	public List<? extends ParamConfig<?>> getParamConfigs();
	
	public ParamConfig<?> getIdParamConfig();
	public ParamConfig<?> getVersionParamConfig();
	
	public CollectionsTemplate<List<ParamConfig<?>>, ParamConfig<?>> templateParamConfigs();
	
	public RulesConfig getRulesConfig();

	@Override
	default MappedModelConfig<T, ?> findIfMapped() {
		return null;
	}
	
	default boolean isRoot() {
		return false;
	}
	
	public interface MappedModelConfig<T, M> extends ModelConfig<T>, MappedConfig<T, M> {
		@Override
		default boolean isMapped() {
			return true;
		}
		
		@Override
		default MappedModelConfig<T, M> findIfMapped() {
			return this;
		}
		
		@Override
		public ModelConfig<M> getMapsToConfig();
	}
	
}
