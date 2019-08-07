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

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.MappedTransientParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.Notification;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *ˇ
 */
@Getter @Setter
public class MappedDefaultTransientParamState<T, M> extends DefaultParamState<T> implements MappedTransientParam<T, M>, NotificationConsumerDelegate<M> {

	private static final long serialVersionUID = 1L;

	
	@JsonIgnore private final Notification.Consumer<M> delegate;
	
	@JsonIgnore private final Param<M> initialMapsTo;
	
	@JsonIgnore private Param<M> detachedMapsTo;
	
	@JsonIgnore private Param<M> mapsToTransient;
	
	
	public interface Creator<T> {
		public EntityState.Model<T> buildMappedTransientModel(MappedDefaultTransientParamState<T, ?> associatedParam, EntityState.Model<?> transientMapsTo);
		
		public Param buildMapsToDetachedParam(MappedDefaultTransientParamState<T, ?> associatedParam);
	}
	
	@JsonIgnore
	private final Creator<T> creator;
	
	public MappedDefaultTransientParamState(Param<M> initialMapsTo, Model<?> parentModel, ParamConfig<T> config, EntityStateAspectHandlers provider, Creator<T> creator) {
		super(parentModel, config, provider);

		this.delegate = new InternalNotificationConsumer<>(this);
		this.creator = creator;
		this.initialMapsTo = initialMapsTo;

		// create detached and link to this mapped parameter
		this.detachedMapsTo = creator.buildMapsToDetachedParam(this);
		this.detachedMapsTo.registerConsumer(this);
	}
	
	@Override
	public void onTypeAssign() {
		Model mappedModel = creator.buildMappedTransientModel(this, detachedMapsTo.findIfNested());
		getType().findIfTransient().assign(mappedModel);
	}
	
	@Override
	public void assignMapsTo() {
		assignMapsTo(initialMapsTo);
	}
	
	@Override
	public void assignMapsTo(String rootMapsToPath) {
		Param<M> mapsToTransient = findParamByPath(rootMapsToPath);
		assignMapsTo(mapsToTransient);	
	}
	
	@Override
	public void assignMapsTo(Param<M> mapsToTransient) {
		Objects.requireNonNull(mapsToTransient, "MapsTo transient param must not be null.");
		
		changeStateTemplate((rt, h, lockId)->{
			// if collection, add element
			Param<M> resolvedMapsTo = mapsToTransient.isCollection() ? mapsToTransient.findIfCollection().add() : mapsToTransient;
			setMapsToTransient(resolvedMapsTo);
			
			copyStateToDetached(resolvedMapsTo.getLeafState());
			
			emitEvent(Action._update, this);
			
			triggerEvents();
			
			return resolvedMapsTo;
		});
	}

	@Override
	public void flush() {
		changeStateTemplate((rt, h, lockId)->{
			if(!isAssinged()) {
				Param<M> resolvedMapsTo = initialMapsTo.isCollection() ? initialMapsTo.findIfCollection().add() : initialMapsTo;
				setMapsToTransient(resolvedMapsTo);
			}
			
			copyStateFromDetached();
			
			emitEvent(Action._update, this);
			
			triggerEvents();
			
			return null;
		});
	}
	
	@Override
	public void unassignMapsTo() {
		changeStateTemplate((rt, h, lockId)->{
			setMapsToTransient(null);
			copyStateToDetached(null);
			
			emitEvent(Action._update, this);
			
			triggerEvents();
			
			return null;
		});
	}
	
	private void copyStateToDetached(M state) {
		detachedMapsTo.setState(state);
	}

	private void copyStateFromDetached() {
		M state = detachedMapsTo.getLeafState();
		mapsToTransient.setState(state);
	}

	@JsonIgnore
	@Override
	public Param<M> getMapsTo() {
		return isAssinged() ? getMapsToTransient() : getDetachedMapsTo();
	}
	
	@JsonIgnore
	@Override
	public boolean isAssinged() {
		return getMapsToTransient()!=null;
	}
	
	@Override
	public boolean requiresConversion() {
		return MappedDefaultParamState.requiresConversion(this);
	}
	
	@Override
	public boolean isTransient() {
		return true;
	}
	
	@Override
	public MappedDefaultTransientParamState<T, M> findIfTransient() {
		return this;
	}
	
	@JsonIgnore
	@Override
	public boolean isMapped() {
		return true;
	}
	
	@Override
	public MappedDefaultTransientParamState<T, M> findIfMapped() {
		return this;
	}
}
