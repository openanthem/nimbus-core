/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.repository;

import com.anthem.oss.nimbus.core.domain.Command;
import com.anthem.oss.nimbus.core.domain.Repo;

/**
 * @author Soham Chakravarti
 *
 */
public interface ModelRepositoryFactory {

	public ModelRepository get(Command cmd); //TODO remove this method once persistence handler is working
	
	public ModelPersistenceHandler getHandler(Repo repo);
	
	public ModelRepository get(Class<?> domainEntity);
}
