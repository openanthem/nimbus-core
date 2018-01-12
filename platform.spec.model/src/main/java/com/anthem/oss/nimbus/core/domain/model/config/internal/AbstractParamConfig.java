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
package com.anthem.oss.nimbus.core.domain.model.config.internal;

import java.lang.annotation.Annotation;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.model.config.AnnotationConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString
public abstract class AbstractParamConfig<P> implements ParamConfig<P> {

	final private String code;

	final private MapsTo.Path mapsTo;
	
	public AbstractParamConfig(String code, MapsTo.Path mapsTo) {
		this.code = code;
		this.mapsTo = mapsTo;
	}
	

	
	abstract public void setDesc(ParamConfig.Desc desc);
	
	abstract public void setValidations(List<AnnotationConfig> validations);
	
	abstract public void setType(ParamType type);
	
	abstract public void setAnnotations(List<Annotation> annotations);
	
}
