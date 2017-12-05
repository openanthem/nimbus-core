/**
 * 
 */
package com.antheminc.oss.nimbus.core.spec.contract.event;

import com.antheminc.oss.nimbus.core.domain.model.state.internal.AbstractEvent;
import com.antheminc.oss.nimbus.core.domain.model.state.internal.AbstractEvent.SuppressMode;

/**
 * @author Rakesh Patel
 * 
 */
public interface EventListener<T extends AbstractEvent<String, ?>> {

	default public void apply(SuppressMode suppressMode) {}
	
	public boolean listen(T event);
	
}
