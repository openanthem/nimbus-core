/**
 *  Copyright 2016-2018 the original author or authors.
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

import com.antheminc.oss.nimbus.domain.defn.AssociatedEntity;
import com.antheminc.oss.nimbus.domain.defn.Converters.ParamConverter;
import com.antheminc.oss.nimbus.domain.defn.Execution;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Mode;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.entity.Findable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
public interface ParamConfig<P> extends EntityConfig<P>, Findable<String> {

	public String getCode();
	public String getBeanName();
	
//	@JsonIgnore M7
	public ParamType getType();
	
	public boolean isLeaf();
	
	@Getter @Setter @ToString
	public static class LabelConfig {
		private String locale; //default en-US
		private String text;
		private String helpText;
	}
	public List<LabelConfig> getLabelConfigs();
	
	
	public List<Execution.Config> getExecutionConfigs();
	
	public List<AnnotationConfig> getValidations();
	
	public List<AnnotationConfig> getUiNatures();
	
	public AnnotationConfig getUiStyles();
	
	// TODO Soham: change to list of notification handler annotations
	public List<AnnotationConfig> getRules();
	
	public Values getValues();
	
	public List<ParamConverter> getConverters();
	
	public List<AssociatedEntity> getAssociatedEntities();

	public void onCreateEvent();
	
	default MapsTo.Mode getMappingMode() {
		return MapsTo.Mode.UnMapped;
	}
	
	@Override
	default MappedParamConfig<P, ?> findIfMapped() {
		return null;
	}
	
	public interface MappedParamConfig<P, M> extends ParamConfig<P>, MappedConfig<P, M> {
		@Override
		default boolean isMapped() {
			return true;
		}
		
		@Override
		default MappedParamConfig<P, M> findIfMapped() {
			return this;
		}
		
		public Path getPath();

		@Override
		default Mode getMappingMode() {
			return MapsTo.getMode(getPath());
		}
		
		public boolean isDetachedWithAutoLoad(); // e.g. @Path(value="/a/b/c/action", linked=false)
		
		@Override
		public ParamConfig<M> getMapsTo();
		
		public ModelConfig<?> getMapsToEnclosingModel();
	}
}
