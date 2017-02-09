/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.repository;

import java.util.List;

import com.anthem.nimbus.platform.spec.model.dsl.ModelEvent;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState;


/**
 * @author Rakesh Patel
 *
 */
public interface ModelPersistenceHandler {
	
	public boolean handle(List<ModelEvent<DomainState<?>>> modelEvents);
	
}
