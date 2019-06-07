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
package com.antheminc.oss.nimbus.domain.model.state.event.listener;

import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.domain.model.state.internal.AbstractEvent.SuppressMode;

/**
 * @author Soham Chakravarti
 *
 */
public interface StateAndConfigEventListener extends EventListener<ModelEvent<Param<?>>> {

	default boolean shouldSuppress(SuppressMode mode) {
		return false;
	}
	
	default boolean shouldAllow(EntityState<?> p) {
		return true;
	}
	
}
