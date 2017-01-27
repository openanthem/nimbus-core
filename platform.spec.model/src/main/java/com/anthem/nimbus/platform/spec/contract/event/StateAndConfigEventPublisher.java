/**
 * 
 */
package com.anthem.nimbus.platform.spec.contract.event;

import com.anthem.nimbus.platform.spec.model.AbstractEvent.SuppressMode;
import com.anthem.nimbus.platform.spec.model.dsl.ModelEvent;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.Config;

/**
 * @author Soham Chakravarti
 *
 */
public interface StateAndConfigEventPublisher extends EventPublisher<ModelEvent<StateAndConfig<?,?>>> {

	/**
	 * 
	 * @param mode
	 * @return
	 */
	default boolean shouldSuppress(SuppressMode mode) {
		return false;
	}
	
	/**
	 * 
	 * @param p
	 * @return
	 */
	default boolean shouldAllow(StateAndConfig<?,?> p) {
		return true;
	}
	
}
