/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
