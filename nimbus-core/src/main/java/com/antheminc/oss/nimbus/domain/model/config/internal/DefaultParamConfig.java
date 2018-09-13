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
package com.antheminc.oss.nimbus.domain.model.config.internal;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.defn.AssociatedEntity;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.defn.Converters.ParamConverter;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.model.config.AnnotationConfig;
import com.antheminc.oss.nimbus.domain.model.config.EventHandlerConfig;
import com.antheminc.oss.nimbus.domain.model.config.ExecutionConfig;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfigType;
import com.antheminc.oss.nimbus.domain.model.config.event.ConfigEventHandlers.OnParamCreateHandler;
import com.antheminc.oss.nimbus.domain.model.config.support.DefaultJsonParamConfigSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@JsonSerialize(using=DefaultJsonParamConfigSerializer.class)
@Getter @Setter @ToString(callSuper=true, of={"code", "beanName", "type"})
public class DefaultParamConfig<P> extends AbstractEntityConfig<P> implements ParamConfig<P>, Serializable {

	private static final long serialVersionUID = 1L;

	final private String code;
	
	@JsonIgnore
	final private String beanName;

	private ParamConfigType type;	
	
	private List<AnnotationConfig> validations;
	
	private List<AnnotationConfig> uiNatures;
	
	@JsonIgnore
	private List<AnnotationConfig> rules;
	
	@JsonIgnore
	private List<AnnotationConfig> extensions;
	
	@JsonIgnore
	public ExecutionConfig executionConfig;
	
	@JsonIgnore 
	private List<ParamConverter> converters;
	
	@JsonIgnore
	private Values values;
	
	@JsonIgnore
	private Set<Label> labels;
	
	@JsonIgnore @Setter 
	private List<AssociatedEntity> associatedEntities;

	protected DefaultParamConfig(String code) {
		this(code, code, generateNextId());
	}
	
	protected DefaultParamConfig(String code, String beanName) {
		super();
		
		Objects.requireNonNull(code, ()->"code in param config must not be null");
		Objects.requireNonNull(beanName, ()->"beanName in param config must not be null");
		
		this.code = code;
		this.beanName = beanName;
		
	}
	
	protected DefaultParamConfig(String code, String beanName, String id) {
		super(id);
		
		Objects.requireNonNull(code, ()->"code in param config must not be null");
		Objects.requireNonNull(beanName, ()->"beanName in param config must not be null");
		Objects.requireNonNull(id, ()->"id in param config must not be null");
		
		this.code = code;
		this.beanName = beanName;
	}


	final public static <T> DefaultParamConfig<T> instantiate(ModelConfig<?> mConfig, String code) {
		return instantiate(mConfig, code, code);
	}
	
	final public static <T> DefaultParamConfig<T> instantiate(ModelConfig<?> mConfig, String code, String beanName) {
		return new DefaultParamConfig<>(code, beanName);
	} 
	
	@JsonIgnore
	@SuppressWarnings("unchecked")
	@Override
	public Class<P> getReferredClass() {
		return (Class<P>)getType().getReferredClass();
	}
	
	@Override
	public boolean isFound(String by) {
		return StringUtils.equals(getCode(), by);
	}
	
	@JsonIgnore
	@Override
	public boolean isLeaf() {
		return !getType().isNested();
	}
	
	@Override
	public <K> ParamConfig<K> findParamByPath(String path) {
		String splits[] = StringUtils.split(path, Constants.SEPARATOR_URI.code);
		return findParamByPath(splits);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <K> ParamConfig<K> findParamByPath(String[] pathArr) {
		if(ArrayUtils.isEmpty(pathArr))
			return null;
		
		/* param is not leaf node: is nested */
		ParamConfigType.Nested<?> mp = getType().findIfNested();
		if(mp != null) {
			return mp.getModelConfig().findParamByPath(pathArr);
		}
		
		/* if param is a leaf node and requested path has more children, then return null */
		if(pathArr.length > 1) return null;
		
		/* param is leaf node */
		ParamConfig<K> p = isFound(pathArr[0]) ? (ParamConfig<K>) this : null;
		return p;
	}
	
	@Override
	public void onCreateEvent() {
		Optional.ofNullable(getEventHandlerConfig())
			.map(EventHandlerConfig::getOnParamCreateAnnotations)
			.ifPresent(
				set->set.stream()
					.forEach(ac-> {
						OnParamCreateHandler<Annotation> handler = getEventHandlerConfig().getOnParamCreateHandler(ac); 
						handler.handle(ac, this);
					})
			);
	}
	
}
