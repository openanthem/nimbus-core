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

	default public void apply(SuppressMode suppressMode) {}
	
	public boolean publish(T event);
	
}
