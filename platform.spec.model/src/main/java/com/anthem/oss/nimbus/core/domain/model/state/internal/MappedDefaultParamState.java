/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.Objects;

import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.StateBuilderContext;
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

	public MappedDefaultParamState(Param<M> mapsTo, Model<?> parentModel, ParamConfig<T> config, StateBuilderContext provider) {
		super(parentModel, config, provider);
		
		Objects.requireNonNull(mapsTo, "MapsTo param must not be null.");
		this.mapsTo = mapsTo;
		
		this.delegate = new InternalNotificationConsumer<>(this);
		
		getMapsTo().registerSubscriber(this);
	}
	
	
	/**
	 * resurrect entity pojo state from model/param state
	 * @return
	 */
//	public T getState2() {
//		if(!isNested())
//			return getState();
//		
//		// create new entity instance
//		T entity = getProvider().getParamStateGateway()._instantiate(this);
//		
//		// assign param values to entity instance attributes
//		findIfNested().getParams().stream()
//			.filter(p->!p.isNested())
//			
//		;
//		
//		return entity;
//	}
}
