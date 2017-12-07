/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.repo;

import java.util.List;

import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.ModelEvent;


/**
 * @author Rakesh Patel
 *
 */
public interface ModelPersistenceHandler {
	
	public boolean handle(List<ModelEvent<Param<?>>> modelEvents);
	
}
