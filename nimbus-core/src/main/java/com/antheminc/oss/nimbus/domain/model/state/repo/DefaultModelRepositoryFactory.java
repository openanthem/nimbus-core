/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.domain.model.state.repo;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;

import lombok.AccessLevel;
import lombok.Getter;


/**
 * Hierarchy based algorithm used to determine custom or platform default implementation
 * 
 * @author Soham Chakravarti
 */

@Getter(value=AccessLevel.PROTECTED)
public class DefaultModelRepositoryFactory implements ModelRepositoryFactory {

	private final BeanResolverStrategy beanResolver;
	
	private final Map<String, ModelRepository> REPO_BEAN_LOOKUP;
		
	public DefaultModelRepositoryFactory(BeanResolverStrategy beanResolver, Map<String, ModelRepository> repoBeanLookup) {
		this.beanResolver = beanResolver;
		this.REPO_BEAN_LOOKUP = repoBeanLookup;
	}

	@Override
	public ModelRepository get(Repo repo) {
		return repo != null ? get(repo.value(), repo.modelRepositoryBean()) : null;
	}
	
	@Override
	public ModelRepository get(Repo.Database db, String extensionBean) {
		if (!Repo.Database.rep_custom.equals(db)) {
			return REPO_BEAN_LOOKUP.get(db.name());
		}

		if (StringUtils.isEmpty(extensionBean)) {
			throw new InvalidConfigException("extensionBean must be defined for rep_custom implementations!");
		}
		
		return getBeanResolver().get(ModelRepository.class, extensionBean);
	}
		
	@Override
	public ModelRepository get(ModelConfig<?> mConfig) {
		if(mConfig.isRemote()) {
			return REPO_BEAN_LOOKUP.get(mConfig.getRepo().remote().name());
		} 			
		return get(mConfig.getRepo());
	}

	public ModelRepository get(Repo.Database db) {
		return get(db, null);
	}
}
