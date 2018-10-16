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
package com.antheminc.oss.nimbus.test.domain.mock;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.antheminc.oss.nimbus.domain.defn.AssociatedEntity;
import com.antheminc.oss.nimbus.domain.defn.Converters.ParamConverter;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.model.config.AnnotationConfig;
import com.antheminc.oss.nimbus.domain.model.config.EventHandlerConfig;
import com.antheminc.oss.nimbus.domain.model.config.ExecutionConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfigType;
import com.antheminc.oss.nimbus.domain.model.config.RulesConfig;
import com.antheminc.oss.nimbus.test.domain.support.utils.PathUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * A mock ParamConfig implementation intended for testing purposes.
 * 
 * @author Tony Lopez
 *
 */
@Getter @Setter 
public class MockParamConfig implements ParamConfig<Object> {

	private List<AssociatedEntity> associatedEntities;
	private String beanName;
	private String code = "";
	private String id = "";
	private List<ParamConverter> converters;
	private Set<Label> labels;
	private EventHandlerConfig eventHandlerConfig;
	private ExecutionConfig executionConfig;
	private boolean leaf;
	private Map<String, ParamConfig<?>> paramConfigMap;
	private Class<Object> referredClass;
	private List<AnnotationConfig> rules;
	private RulesConfig rulesConfig;
	private ParamConfigType type;
	private List<AnnotationConfig> uiNatures;
	private AnnotationConfig uiStyles;
	private List<AnnotationConfig> validations;
	private Values values;
	private List<AnnotationConfig> extensions;

	/*
	 * (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.config.EntityConfig#findParamByPath(java.lang.String)
	 */
	@Override
	public <K> ParamConfig<K> findParamByPath(String path) {
		return (ParamConfig<K>) this.paramConfigMap.get(path);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.config.EntityConfig#findParamByPath(java.lang.String[])
	 */
	@Override
	public <K> ParamConfig<K> findParamByPath(String[] pathArr) {
		return PathUtils.findFirstByPath(pathArr, k -> this.findParamByPath(k));
	}

	/*
	 * (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.entity.Findable#isFound(java.lang.Object)
	 */
	@Override
	public boolean isFound(String by) {
		return this.paramConfigMap.containsValue(by);
	}

	/**
	 * Puts a <tt>ParamConfig</tt> into an internal map by <tt>path</tt> as key.
	 * 
	 * @param paramConfig the <tt>ParamConfig</tt> to store
	 * @param path the key to store <tt>paramConfig</tt> under
	 */
	public <K> void putParamConfig(ParamConfig<K> paramConfig, String path) {
		this.paramConfigMap.put(path, paramConfig);
	}

	@Override
	public void onCreateEvent() {
		
	}
}
