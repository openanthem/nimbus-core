/**
 * 
 */
package com.anthem.nimbus.platform.spec.contract.event;

import com.anthem.nimbus.platform.spec.model.AbstractEvent.SuppressMode;
import com.anthem.nimbus.platform.spec.model.dsl.ModelEvent;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public interface StateAndConfigEventPublisher extends EventPublisher<ModelEvent<Param<?>>> {

	default boolean shouldSuppress(SuppressMode mode) {
		return false;
	}
	
	default boolean shouldAllow(DomainState<?> p) {
		return true;
	}
	
}
