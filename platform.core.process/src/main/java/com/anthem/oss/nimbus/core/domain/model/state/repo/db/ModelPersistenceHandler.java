/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import java.util.List;

import com.anthem.oss.nimbus.core.domain.model.state.DomainState;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;


/**
 * @author Rakesh Patel
 *
 */
public interface ModelPersistenceHandler {
	
	public boolean handle(List<ModelEvent<DomainState<?>>> modelEvents);
	
}
