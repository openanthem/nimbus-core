/**  
 * Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.domain.model.state.repo.event;

import java.lang.annotation.Annotation;

import com.antheminc.oss.nimbus.domain.EventHandler;
import com.antheminc.oss.nimbus.domain.defn.event.RepoEvent.OnPersist;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig.MappedModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public final class RepoEventHandlers {

	@EventHandler(OnPersist.class)
	public interface OnPersistHandler<A extends Annotation> {
		
		default boolean shouldAllow(A configuredAnnotation, Param<?> param) {
			return true;
		} 
		
		public void onPersist(A configuredAnnotation, MappedModelConfig<?, ?> mappedModelConfig, Param<?> coreParam);
	}
}
