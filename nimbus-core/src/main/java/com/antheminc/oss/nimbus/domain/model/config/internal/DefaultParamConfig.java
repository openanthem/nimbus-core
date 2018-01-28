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
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.defn.AssociatedEntity;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.defn.Converters.ParamConverter;
import com.antheminc.oss.nimbus.domain.defn.Execution;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.model.config.AnnotationConfig;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true, of={"code", "beanName", "type"})
public class DefaultParamConfig<P> extends AbstractEntityConfig<P> implements ParamConfig<P>, Serializable {

	private static final long serialVersionUID = 1L;

	final private String code;
	final private String beanName;

	private ParamType type;	

	private Desc desc = new Desc();
	
	private List<AnnotationConfig> validations;
	
	private List<AnnotationConfig> uiNatures;
	
	@JsonIgnore
	private List<AnnotationConfig> rules;
	
	
	@JsonIgnore
	private List<Execution.Config> executionConfigs;

//	@JsonIgnore M7
//M8	private ParamConfig<StateContextEntity> contextParam;
	
	@JsonIgnore 
	private List<ParamConverter> converters;
	
	@JsonIgnore
	private Values values;
	
	@JsonIgnore @Setter 
	private List<AssociatedEntity> associatedEntities;


//	public static class StateContextConfig<P> extends DefaultParamConfig<P> {
//		private static final long serialVersionUID = 1L;
//
//		public StateContextConfig(String code, String beanName) {
//			super(code, beanName);
//		}
//		
//		@Override
//		final public ParamConfig<StateContextEntity> getContextParam() {
//			return null;
//		}
//		
//		@Override
//		final public void setContextParam(ParamConfig<StateContextEntity> runtimeConfig) {
//			//do nothing
//		}
//	}
	
	protected DefaultParamConfig(String code) {
		this(code, code);
	}
	
	protected DefaultParamConfig(String code, String beanName) {
		Objects.requireNonNull(code, ()->"code in param config must not be null");
		Objects.requireNonNull(beanName, ()->"beanName in param config must not be null");
		
		this.code = code.intern();
		this.beanName = beanName.intern();
	}
	
	final public static <T> DefaultParamConfig<T> instantiate(ModelConfig<?> mConfig, String code) {
		return instantiate(mConfig, code, code);
	}
	
	final public static <T> DefaultParamConfig<T> instantiate(ModelConfig<?> mConfig, String code, String beanName) {
//		if(mConfig.getReferredClass()==StateContextEntity.class)
//			return new DefaultParamConfig.StateContextConfig<>(code, beanName);
		
		return new DefaultParamConfig<>(code, beanName);
	} 
	

	@SuppressWarnings("unchecked")
	@Override
	public Class<P> getReferredClass() {
		return (Class<P>)getType().getReferredClass();
	}
	
	@Override
	public boolean isFound(String by) {
		return StringUtils.equals(getCode(), by);
	}
	
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
		ParamType.Nested<?> mp = getType().findIfNested();
		if(mp != null) {
			return mp.getModel().findParamByPath(pathArr);
		}
		
		/* if param is a leaf node and requested path has more children, then return null */
		if(pathArr.length > 1) return null;
		
		/* param is leaf node */
		ParamConfig<K> p = isFound(pathArr[0]) ? (ParamConfig<K>) this : null;
		return p;
	}
	
}
