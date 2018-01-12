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

import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class MappedDefaultParamState<T, M> extends DefaultParamState<T> implements EntityState.MappedParam<T, M>, NotificationConsumerDelegate<M> {

	private static final long serialVersionUID = 1L;

	@JsonIgnore private final Param<M> mapsTo;
	
	@JsonIgnore private final Notification.Consumer<M> delegate;

	public static class MappedLeafState<T, M> extends MappedDefaultParamState<T, M> implements LeafParam<T> {
		private static final long serialVersionUID = 1L;
		
		public MappedLeafState(Param<M> mapsTo, Model<?> parentModel, ParamConfig<T> config, EntityStateAspectHandlers provider) {
			super(mapsTo, parentModel, config, provider);
		}
	}
	
	public MappedDefaultParamState(Param<M> mapsTo, Model<?> parentModel, ParamConfig<T> config, EntityStateAspectHandlers provider) {
		super(parentModel, config, provider);
		
		Objects.requireNonNull(mapsTo, "MapsTo param must not be null.");
		this.mapsTo = mapsTo;
		
		this.delegate = new InternalNotificationConsumer<M>(this) {
			@Override
			protected void onEventEvalProcess(Notification<M> event) {
				// fire rules at root level upon completion of all set actions
				getRootExecution().fireRules();
				
				// evaluate BPM
				evaluateProcessFlow();
			}
		};
		
		getMapsTo().registerConsumer(this);
	}
}
