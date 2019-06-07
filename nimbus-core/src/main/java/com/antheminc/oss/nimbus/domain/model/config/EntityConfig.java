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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Soham Chakravarti
 *
 */
public interface EntityConfig<T> {

	public String getId();
	
	@JsonIgnore
	public Class<T> getReferredClass();
	
	@JsonIgnore
	public EventHandlerConfig getEventHandlerConfig();
	
	public <K> ParamConfig<K> findParamByPath(String path);
	public <K> ParamConfig<K> findParamByPath(String[] pathArr);

	
	default public boolean hasRules() {
		return getRulesConfig()!=null;
	}
	
	@JsonIgnore
	public RulesConfig getRulesConfig();
	
	@JsonIgnore
	default boolean isMapped() {
		return false;
	}
	
	default public MappedConfig<T, ?> findIfMapped() {
		return null;
	}

	public interface MappedConfig<T, M> extends EntityConfig<T> {
		@Override
		default boolean isMapped() {
			return true;
		}
		
		@Override
		default public MappedConfig<T, ?> findIfMapped() {
			return this;
		}
		
		public EntityConfig<M> getMapsToConfig();
	}
	
	public enum Scope {
		LOCAL,
		REMOTE;		
	}
}
