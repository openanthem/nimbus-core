/**
 * 
 */
package com.anthem.oss.nimbus.core.spec.contract.event;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.internal.AbstractEvent;

/**
 * @author Rakesh Patel
 * 
 */
public interface BulkEventListener<T extends AbstractEvent<String, Param<?>>> {

	public boolean listen(List<T> events);
	
}
