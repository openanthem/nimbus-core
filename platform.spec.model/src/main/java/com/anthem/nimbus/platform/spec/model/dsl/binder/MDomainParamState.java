/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.util.Objects;

import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class MDomainParamState<T, M> extends DomainParamState<T> implements DomainState.MappedParam<T, M>, NotificationConsumerDelegate<M> {

	private static final long serialVersionUID = 1L;

	@JsonIgnore private final Param<M> mapsTo;
	
	@JsonIgnore private final Notification.Consumer<M> delegate;

	public MDomainParamState(Param<M> mapsTo, Model<?> parentModel, ParamConfig<T> config, StateAndConfigSupportProvider provider) {
		super(parentModel, config, provider);
		
		Objects.requireNonNull(mapsTo, "MapsTo param must not be null.");
		this.mapsTo = mapsTo;
		
		this.delegate = new InternalNotificationConsumer<>(this);
		
		getMapsTo().registerSubscriber(this);
	}
}
