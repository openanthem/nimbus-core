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
package com.antheminc.oss.nimbus.domain.model.state.internal;

import java.util.Objects;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.MappedTransientParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionRuntime;
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

	@JsonIgnore private transient Param<M> mapsToTransient;
	
	@JsonIgnore private final Notification.Consumer<M> delegate;
	
	@JsonIgnore private final Param<M> initialMapsTo;
	
	@JsonIgnore 
	private RemnantState<Boolean> isAssignedState = this.new RemnantState<Boolean>(false);
	
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

	}
	
	@Override
	protected void initStateInternal() {
		// initialize with shell: detached entity
		resetToDetachedMapping();
		
		super.initStateInternal();
	}

	@Override
	public void onTypeAssign() {
		Param mapsToTransientDetached = creator.buildMapsToDetachedParam(this);
		setMapsToTransient(mapsToTransientDetached);
		mapsToTransientDetached.registerConsumer(this);
		
		assignType();
		
		fireRules();
	}
	
	public void assignType() {
		Model mappedModel = creator.buildMappedTransientModel(this, getMapsTo().findIfNested());
		getType().findIfTransient().assign(mappedModel);
	}
	
//	@Override
//	public void fireRules() {
//		if(isAssinged())
//			super.fireRules();
//	}
	
	@JsonIgnore
	@Override
	public Param<M> getMapsTo() {
		return mapsToTransient;
	}
	
	@JsonIgnore
	@Override
	public boolean isAssinged() {
		return this.isAssignedState.getCurrState();
	}
	
	public void setAssigned(boolean isAssigned) {
		this.isAssignedState.setState(isAssigned);
	}
	
	@Override
	public void flush() {
		assignMapsTo();
	}
	
	@Override
	public void assignMapsTo() {
		T oldState = getLeafState();
		assignMapsTo(initialMapsTo);
		setState(oldState);
	}
	
	@Override
	public void assignMapsTo(Param<M> mapsToTransient) {
		assignMapsToInternal(mapsToTransient, true);
		
		triggerEvents();
	}
	
	private void assignMapsToInternal(Param<M> mapsToTransient, boolean isAssigned) {
		Objects.requireNonNull(mapsToTransient, "MapsTo transient param must not be null.");
		
		if(getMapsTo()==mapsToTransient)
			return;
		
		changeStateTemplate((rt, h, lockId)->{
			unassignMapsToInternal();
			
			final Param<M> resolvedMapsTo;
			
			//TODO: 1. create model for this type (Mapped) based on passed in mapsTo
			// 1.1 If passed in mapsTo is collection, then create mapsTo (shell) element which "might" get added to collection upon setState of this mapped:: createElement is not same addElement
			final Action a;
			if(mapsToTransient.isCollection()) {
				resolvedMapsTo = mapsToTransient.findIfCollection().add();
				a = Action._new;
			} else {
				resolvedMapsTo = mapsToTransient;
				a = Action._replace;
			}
			
			// 2. hook up notifications to mapsTo
			resolvedMapsTo.registerConsumer(this);
			setMapsToTransient(resolvedMapsTo);
			setAssigned(isAssigned);
			
			// 3. create new mapped model based on mapsTo
			assignType();
			
			// 4. fire rules
			fireRules();
			
			// 5. emit event
			emitEvent(a, this);
			
			return resolvedMapsTo;
		});
	}
	
	@Override
	public void unassignMapsTo() {
		changeStateTemplate((rt, h, lockId)->{
			unassignMapsToInternal();
			
			resetToDetachedMapping();
			
			triggerEvents();
			
			return null;
		});
	}
	
	private void unassignMapsToInternal() {
		if(getMapsTo()==null)
			return;
		
		getMapsTo().deregisterConsumer(this);
		
		getType().findIfTransient().unassign();
		
		setMapsToTransient(null);
		
		setAssigned(false);
	}
	
	private void resetToDetachedMapping() {
		Param mapsToTransientDetached = creator.buildMapsToDetachedParam(this);
		assignMapsToInternal(mapsToTransientDetached, false);
	}

	@Override
	public boolean requiresConversion() {
		return MappedDefaultParamState.requiresConversion(this);
	}
	
	@Override
	public void assignMapsTo(String rootMapsToPath) {
		Param<M> mapsToTransient = findParamByPath(rootMapsToPath);
		assignMapsTo(mapsToTransient);		
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
