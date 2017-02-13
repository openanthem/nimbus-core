/**
 * 
 */
package com.anthem.nimbus.platform.spec.contract.event;

import com.anthem.oss.nimbus.core.domain.model.state.DomainState;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.AbstractEvent.SuppressMode;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.Param;

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
