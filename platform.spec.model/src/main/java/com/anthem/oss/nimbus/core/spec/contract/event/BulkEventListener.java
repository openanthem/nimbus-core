/**
 * 
 */
package com.antheminc.oss.nimbus.core.spec.contract.event;

import java.util.List;

import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.internal.AbstractEvent;

/**
 * @author Rakesh Patel
 * 
 */
public interface BulkEventListener<T extends AbstractEvent<String, Param<?>>> {

	public boolean listen(List<T> events);
	
}
