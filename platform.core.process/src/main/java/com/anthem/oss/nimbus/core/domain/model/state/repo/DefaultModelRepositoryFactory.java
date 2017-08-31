/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.definition.Repo;


/**
 * Hierarchy based algorithm used to determine custom or platform default implementation
 * 
 * @author Soham Chakravarti
 */
public class DefaultModelRepositoryFactory implements ModelRepositoryFactory {

	private final BeanResolverStrategy beanResolver;
	
	public DefaultModelRepositoryFactory(BeanResolverStrategy beanResolver) {
		this.beanResolver = beanResolver;
	}
	
	
	@Override
	public ModelRepository get(Repo repo) {
		return beanResolver.get(ModelRepository.class, repo.value().name());
	}
	

	@Override
	public ModelPersistenceHandler getHandler(Repo repo) {
		return beanResolver.get(ModelPersistenceHandler.class, repo.value().name()+"_handler");
	}
	
}
