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
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.CollectionUtils;

import com.antheminc.oss.nimbus.domain.defn.AssociatedEntity;
import com.antheminc.oss.nimbus.domain.defn.ConfigNature.Ignore;
import com.antheminc.oss.nimbus.domain.defn.Converters.ParamConverter;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Mode;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.entity.Findable;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Soham Chakravarti
 *
 */
public interface ParamConfig<P> extends EntityConfig<P>, Findable<String> {

	public String getCode();
	public String getBeanName();
	
//	@JsonIgnore M7
	public ParamConfigType getType();
	
	public boolean isLeaf();
	
	public Set<Label> getLabels();
	
	public ExecutionConfig getExecutionConfig();
	
	public List<AnnotationConfig> getValidations();
	
	public List<AnnotationConfig> getUiNatures();
	
	public AnnotationConfig getUiStyles();
	
	// TODO Soham: change to list of notification handler annotations
	public List<AnnotationConfig> getRules();
	
	public List<AnnotationConfig> getExtensions();
	
	public Values getValues();
	
	public List<ParamConverter> getConverters();
	
	public List<AssociatedEntity> getAssociatedEntities();

	public void onCreateEvent();
	
	@JsonIgnore
	default boolean isTransient() {
		return false;
	}
	
	@JsonIgnore
	default MappedTransientParamConfig<P, ?> findIfTransient() {
		return null;
	}
	
	@JsonIgnore
	default MapsTo.Mode getMappingMode() {
		return MapsTo.Mode.UnMapped;
	}
	
	@Override
	default MappedParamConfig<P, ?> findIfMapped() {
		return null;
	}
	
	@JsonIgnore
	default boolean containsIgnoreListener(ListenerType listenerType) {
		if(!CollectionUtils.isEmpty(getExtensions())) {
			return getExtensions().stream()
					.map(AnnotationConfig::getAnnotation)
					.filter(a->a.annotationType()==Ignore.class)
						.map(Ignore.class::cast)
							.map(Ignore::listeners)
							.filter(array->ArrayUtils.contains(array,listenerType))
							.findFirst()
							.isPresent();
		}
		return false;
	}

	
	public interface MappedParamConfig<P, M> extends ParamConfig<P>, MappedConfig<P, M> {
		@Override
		boolean isMapped();
		
		@Override
		MappedParamConfig<P, M> findIfMapped();
		
		public Path getPath();

		@Override
		default Mode getMappingMode() {
			return MapsTo.getMode(getPath());
		}
		
		public boolean isDetachedWithAutoLoad(); // e.g. @Path(value="/a/b/c/action", linked=false)
		
		@Override
		public ParamConfig<M> getMapsToConfig();
		
		public ModelConfig<?> getMapsToEnclosingModel();
	}
	
	public interface MappedTransientParamConfig<P, M> extends MappedParamConfig<P, M> {
		@Override
		default boolean isTransient() {
			return true;
		}
		
		@Override @JsonIgnore
		default MappedTransientParamConfig<P, ?> findIfTransient() {
			return this;
		}
		
		MappedParamConfig<P, M> getSimulatedDetached();
	}
}
