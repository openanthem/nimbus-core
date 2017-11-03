/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.Objects;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.MappedTransientParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionRuntime;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
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
	
	@FunctionalInterface
	public interface Creator<T> {
		public EntityState.Model<T> apply(MappedDefaultTransientParamState<T, ?> associatedParam, EntityState.Model<?> transientMapsTo);
	}
	
	private final Creator<T> creator;
	
	public MappedDefaultTransientParamState(Param<M> initialMapsTo /* remove if not needed */, Model<?> parentModel, ParamConfig<T> config, EntityStateAspectHandlers provider, Creator<T> creator) {
		super(parentModel, config, provider);

		this.delegate = new InternalNotificationConsumer<>(this);
		this.creator = creator;
		
		// initialize with shell
		//assignMapsTo(mapsTo);
	}
	
	@Override
	protected void initStateInternal() {
		if(isAssinged())
			super.initStateInternal();
	}

	@Override
	public void fireRules() {
		if(isAssinged())
			super.fireRules();
	}
	
	@Override
	public Param<M> getMapsTo() {
		return mapsToTransient;
	}
	
	@Override
	public void assignMapsTo(Param<M> mapsToTransient) {
		Objects.requireNonNull(mapsToTransient, "MapsTo transient param must not be null.");
		
		if(getMapsTo()==mapsToTransient)
			return;
		
		ExecutionRuntime execRt = resolveRuntime();
		String lockId = execRt.tryLock();
		try {
			unassignMapsTo();
			
			final Param<M> resolvedMapsTo;
			
			//TODO: 1. create model for this type (Mapped) based on passed in mapsTo
			// 1.1 If passed in mapsTo is collection, then create mapsTo (shell) element which "might" get added to collection upon setState of this mapped:: createElement is not same addElement
			final Action a;
			if(mapsToTransient.isCollection()) {
				resolvedMapsTo = mapsToTransient.findIfCollection().createElement();
				a = Action._new;
			} else {
				resolvedMapsTo = mapsToTransient;
				a = Action._replace;
			}
			
			// 2. hook up notifications to mapsTo
			resolvedMapsTo.registerConsumer(this);
			setMapsToTransient(resolvedMapsTo);
			
			// 3. create new mapped model based on mapsTo
			Model mappedModel = creator.apply(this, getMapsTo().findIfNested());
			getType().findIfTransient().assign(mappedModel);
			
			// 4. fire rules
			fireRules();
			
			// 5. emit event
			emitEvent(a, this);
			
		} finally {
			execRt.tryUnlock(lockId);
		}
	}
	
	@Override
	public void unassignMapsTo() {
		if(!isAssinged()) 
			return;
		
		getMapsTo().deregisterConsumer(this);
		
		getType().findIfTransient().unassign();
		
		setMapsToTransient(null);
	}
}
