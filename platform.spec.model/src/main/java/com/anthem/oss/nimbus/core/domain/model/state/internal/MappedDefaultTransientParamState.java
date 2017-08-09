/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.Objects;

import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.MappedTransientParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Setter;

/**
 * @author Soham Chakravarti
 *ˇ
 */
@Setter
public class MappedDefaultTransientParamState<T, M> extends MappedDefaultParamState<T, M> implements MappedTransientParam<T, M> {

	private static final long serialVersionUID = 1L;

	@JsonIgnore private transient Param<M> mapsToTransient;
	
	public MappedDefaultTransientParamState(Param<M> mapsTo, Model<?> parentModel, ParamConfig<T> config, EntityStateAspectHandlers provider) {
		super(mapsTo, parentModel, config, provider);
		
		// initialize with shell
		assignMapsTo(mapsTo);
	}

	@Override
	public Param<M> getMapsTo() {
		return mapsToTransient;
	}
	
	@Override
	public void assignMapsTo(Param<M> mapsToTransient) {
		Objects.requireNonNull(mapsToTransient, "MapsTo transient param must not be null.");
		
		unassignMapsTo();
		
		mapsToTransient.registerSubscriber(this);
		
		this.mapsToTransient = mapsToTransient;
	}
	
	@Override
	public void unassignMapsTo() {
		if(!isAssinged()) 
			return;
		
		getMapsTo().deregisterSubscriber(this);
	}
}
