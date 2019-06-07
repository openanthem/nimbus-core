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
package com.antheminc.oss.nimbus.domain.model.state.internal;

import java.util.Objects;

import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.Notification;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString(callSuper=true, of="mapsTo")
public class MappedDefaultParamState<T, M> extends DefaultParamState<T> implements EntityState.MappedParam<T, M>, NotificationConsumerDelegate<M> {

	private static final long serialVersionUID = 1L;

	@JsonIgnore private final Param<M> mapsTo;
	
	@JsonIgnore private final Notification.Consumer<M> delegate;

	public static class MappedLeafState<T, M> extends MappedDefaultParamState<T, M> implements LeafParam<T> {
		private static final long serialVersionUID = 1L;
		
		public MappedLeafState(Param<M> mapsTo, Model<?> parentModel, ParamConfig<T> config, EntityStateAspectHandlers provider) {
			super(mapsTo, parentModel, config, provider);
		}
		
		@JsonIgnore
		@Override
		public boolean isLeaf() {
			return true;
		}
		
		@Override
		public MappedLeafState<T, M> findIfLeaf() {
			return this;
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
	
	@JsonIgnore
	@Override
	public boolean isMapped() {
		return true;
	}
	
	@Override
	public MappedDefaultParamState<T, M> findIfMapped() {
		return this;
	}
	
	@JsonIgnore
	public boolean requiresConversion() {
		return requiresConversion(this);
	}
	
	@JsonIgnore
	public static <T, M> boolean requiresConversion(MappedParam<T, M> mappedParam) {
		if(mappedParam.isLeaf()) 
			return false;
		
		if(mappedParam.isTransient() && !mappedParam.findIfTransient().isAssinged()) { // when transient is not assigned
			Class<?> mappedClass = mappedParam.getType().getConfig().getReferredClass();
			Class<?> mapsToClass = mappedParam.getType().getConfig().findIfNested().getModelConfig().findIfMapped().getMapsToConfig().getReferredClass();
			
			return (mappedClass!=mapsToClass);
		}
		
		Class<?> mappedClass = mappedParam.getType().findIfNested().getModel().getConfig().getReferredClass();
		Class<?> mapsToClass = mappedParam.getMapsTo().getType().findIfNested().getModel().getConfig().getReferredClass();

		// conversion required when mappedClass and mapsToClass are NOT same
		return (mappedClass!=mapsToClass);
	}
}
