/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.Objects;

import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.MappedModel;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class MappedDefaultModelState<T, M> extends DefaultModelState<T> implements MappedModel<T, M>{

    private static final long serialVersionUID = 1L;

    @JsonIgnore private final Model<M> mapsTo;

	public MappedDefaultModelState(Model<M> mapsTo, Param<T> associatedParam, ModelConfig<T> config, EntityStateAspectHandlers provider) {
    	super(associatedParam, config, provider);
    	
    	Objects.requireNonNull(mapsTo, "MapsTo model must not be null.");
    	this.mapsTo = mapsTo;
 	}

}
