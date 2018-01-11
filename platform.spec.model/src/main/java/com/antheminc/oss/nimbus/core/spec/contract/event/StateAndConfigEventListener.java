/**
 * 
 */
package com.antheminc.oss.nimbus.core.spec.contract.event;

import com.antheminc.oss.nimbus.core.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.core.domain.model.state.internal.AbstractEvent.SuppressMode;

/**
 * @author Soham Chakravarti
 *
 */
public interface StateAndConfigEventListener extends EventListener<ModelEvent<Param<?>>> {

	default boolean shouldSuppress(SuppressMode mode) {
		return false;
	}
	
	default boolean shouldAllow(EntityState<?> p) {
		return true;
	}
	
}
