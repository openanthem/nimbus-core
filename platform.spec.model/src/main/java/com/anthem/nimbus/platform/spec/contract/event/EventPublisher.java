/**
 * 
 */
package com.anthem.nimbus.platform.spec.contract.event;

import com.anthem.nimbus.platform.spec.model.AbstractEvent;
import com.anthem.nimbus.platform.spec.model.AbstractEvent.SuppressMode;

/**
 * @author Rakesh Patel
 * 
 */
public interface EventPublisher<T extends AbstractEvent<String, ?>> {

	/**
	 * 
	 * @param suppressMode
	 */
	default public void apply(SuppressMode suppressMode) {}
	
	/**
	 * 
	 * @param event
	 * @return
	 */
	public boolean publish(T event);
	
}
