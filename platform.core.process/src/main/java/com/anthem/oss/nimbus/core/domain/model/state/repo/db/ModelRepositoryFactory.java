/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.definition.Repo;

/**
 * @author Soham Chakravarti
 *
 */
public interface ModelRepositoryFactory {

	public ModelRepository get(Command cmd); //TODO remove this method once persistence handler is working
	
	public ModelPersistenceHandler getHandler(Repo repo);
	
	public ModelRepository get(Class<?> domainEntity);
}
