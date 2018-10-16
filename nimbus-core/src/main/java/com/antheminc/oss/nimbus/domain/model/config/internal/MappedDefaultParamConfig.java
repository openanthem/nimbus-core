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

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.DetachedState;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.LoadState;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Mode;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Nature;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig.MappedParamConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString(callSuper=true, of={"mapsToConfig", "path"})
public class MappedDefaultParamConfig<P, M> extends DefaultParamConfig<P> implements MappedParamConfig<P, M>, Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore final private ModelConfig<?> mapsToEnclosingModel;
	
	@JsonIgnore	final private ParamConfig<M> mapsToConfig;

	@JsonIgnore final private Path path;
	
	public static class NoConversion<P, M> extends MappedDefaultParamConfig<P, M> {
		private static final long serialVersionUID = 1L;
		
		public NoConversion(ModelConfig<?> mapsToEnclosingModel, ParamConfig<M> mapsTo) {
			super(mapsTo.getCode(), mapsTo.getBeanName(), mapsToEnclosingModel, mapsTo, createNewImplicitMapping("", true), mapsTo.getId());
			
			init(mapsTo);
		}
		
		private void init(ParamConfig<M> mapsTo) {
			setConverters(mapsTo.getConverters());
			setExecutionConfig(mapsTo.getExecutionConfig());
			setRules(mapsTo.getRules());
			setType(mapsTo.getType());
			setUiNatures(mapsTo.getUiNatures());
			setUiStyles(mapsTo.getUiStyles());
			setValidations(mapsTo.getValidations());
			setLabels(mapsTo.getLabels());
			setValues(mapsTo.getValues());
			setEventHandlerConfig(mapsTo.getEventHandlerConfig());
		}

	}
	
	public MappedDefaultParamConfig(String code, ModelConfig<?> mapsToEnclosingModel, ParamConfig<M> mapsTo, Path path) {
		this(code, code, mapsToEnclosingModel, mapsTo, path);
	}
	
	public MappedDefaultParamConfig(String code, String beanName, ModelConfig<?> mapsToEnclosingModel, ParamConfig<M> mapsToConfig, Path path) {
		super(code, beanName);
		this.mapsToEnclosingModel = mapsToEnclosingModel;
		this.mapsToConfig = mapsToConfig;
		this.path = path;
	}
	
	public MappedDefaultParamConfig(String code, String beanName, ModelConfig<?> mapsToEnclosingModel, ParamConfig<M> mapsToConfig, Path path, String id) {
		super(code, beanName, id);
		this.mapsToEnclosingModel = mapsToEnclosingModel;
		this.mapsToConfig = mapsToConfig;
		this.path = path;
	}
	
	@Override
	public boolean isMapped() {
		return true;
	}
	
	@JsonIgnore
	@Override
	public boolean isDetachedWithAutoLoad() {
		
		if(getMappingMode() == Mode.MappedDetached
				&& getPath().detachedState() != null
				&& getPath().detachedState().loadState() == LoadState.AUTO
				&& StringUtils.isNotBlank(getPath().value())) {
			
			return true;
		}
		return false;
	}
	
	
	public static MapsTo.Path createNewImplicitMapping(String mappedPath, boolean linked) {
		return createNewImplicitMapping("", true, new DetachedState() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return MapsTo.DetachedState.class;
			}
			
			@Override
			public boolean manageState() {
				return false;
			}
			
			@Override
			public LoadState loadState() {
				return LoadState.PROVIDED;
			}
			
			@Override
			public Cache cacheState() {
				return Cache.rep_none;
			}
		});
	}
	
	public static MapsTo.Path createNewImplicitMapping(String mappedPath, boolean linked, DetachedState detachedState) {
		return createNewImplicitMapping(mappedPath, linked, "", detachedState);
	}
	
	public static MapsTo.Path createNewImplicitMapping(String mappedPath, boolean linked, String colElemPath, DetachedState detachedState) {
		return new MapsTo.Path() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return MapsTo.Path.class;
			}
			
			@Override
			public String value() {
				return mappedPath;
			}
			
			@Override
			public boolean linked() {
				return linked;
			}
			
			@Override
			public String colElemPath() {
				return colElemPath;
			}
			
			@Override
			public DetachedState detachedState() {
				return detachedState;
			}
			
			@Override
			public Nature nature() {
				return Nature.Default;
			}
			
			@Override
			public String toString() {
				return new StringBuilder().append(MapsTo.Path.class)
							.append(" value: ").append(value())
							.append(" linked: ").append(linked())
							.toString();
			}
		};
	}

	@Override
	public MappedParamConfig<P, M> findIfMapped() {
		return this;
	}
}
