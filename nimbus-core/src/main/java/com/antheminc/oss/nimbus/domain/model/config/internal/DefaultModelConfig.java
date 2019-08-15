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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.support.pojo.CollectionsTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true, of={"alias", "referredClass"}) @RequiredArgsConstructor
public class DefaultModelConfig<T> extends AbstractEntityConfig<T> implements ModelConfig<T>, Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore private final Class<T> referredClass;
	
	@JsonIgnore private String alias;
	
	@JsonIgnore private Domain domain;
	@JsonIgnore private Model model;
	
	@JsonIgnore private Repo repo;
	
	@JsonIgnore private boolean remote;
	
	private List<ParamConfig<?>> paramConfigs;
	
	@JsonIgnore private transient ParamConfig<?> idParamConfig;
	
	@JsonIgnore	private transient ParamConfig<?> versionParamConfig;
	
	@JsonIgnore
	private final transient CollectionsTemplate<List<ParamConfig<?>>, ParamConfig<?>> templateParamConfigs = new CollectionsTemplate<>(
			() -> getParamConfigs(), (p) -> setParamConfigs(p), () -> new LinkedList<>());

	@Override @JsonIgnore
	public CollectionsTemplate<List<ParamConfig<?>>, ParamConfig<?>> templateParamConfigs() {
		return templateParamConfigs;
	}

	@Override @JsonIgnore 
	public String getDomainLifecycle() {
		return Optional.ofNullable(getDomain()).map(Domain::lifecycle).orElse(null);
	}
	
	@Override
	public <K> ParamConfig<K> findParamByPath(String path) {
		// handle scenario if path = "/"
		String splits[] = StringUtils.equals(Constants.SEPARATOR_URI.code, StringUtils.trimToNull(path))
				? new String[] { path } : StringUtils.split(path, Constants.SEPARATOR_URI.code);

		return findParamByPath(splits);
	}
	
	@Override
	public <K> ParamConfig<K> findParamByPath(String[] pathArr) {
		if(ArrayUtils.isEmpty(pathArr)) return null;
		
		String nPath = pathArr[0];
		
		@SuppressWarnings("unchecked")
		ParamConfig<K> p = (ParamConfig<K>)templateParamConfigs().find(nPath);
		if(p == null) return null;
		
		if(pathArr.length == 1) { //last one
			return p;
		}
		
		return p.findParamByPath(ArrayUtils.remove(pathArr, 0));
	}
	
	public String getRepoAlias() {
		return getRepo() != null && StringUtils.isNotBlank(getRepo().alias()) ? getRepo().alias() : getAlias();
	}
	
}
