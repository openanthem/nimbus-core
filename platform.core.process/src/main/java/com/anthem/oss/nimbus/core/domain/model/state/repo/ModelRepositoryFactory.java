/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo;

import com.anthem.oss.nimbus.core.domain.definition.Repo;

/**
 * @author Soham Chakravarti
 *
 */
public interface ModelRepositoryFactory {

	public ModelRepository get(Repo repo);

	public ModelPersistenceHandler getHandler(Repo repo);
}
