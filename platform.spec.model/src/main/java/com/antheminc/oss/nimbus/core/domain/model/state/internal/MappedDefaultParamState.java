/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.internal;

import java.util.Objects;

import com.antheminc.oss.nimbus.core.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.core.domain.model.state.Notification;
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
