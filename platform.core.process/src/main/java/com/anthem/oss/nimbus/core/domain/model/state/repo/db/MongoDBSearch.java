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
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.Assert;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.entity.SearchCriteria;

import lombok.Getter;

/**
 * @author AC67870
 *
 */
@Getter
public abstract class MongoDBSearch implements DBSearch {

	private final MongoOperations mongoOps;
	
	private final CommandMessageConverter converter;
	
	private final DomainConfigBuilder domainConfigBuilder;
	
	private final BeanResolverStrategy beanResolver;
	
	public MongoDBSearch(BeanResolverStrategy beanResolver) {
		this.beanResolver = beanResolver;
		this.mongoOps = beanResolver.get(MongoOperations.class);
		this.domainConfigBuilder = beanResolver.get(DomainConfigBuilder.class);
		this.converter = beanResolver.get(CommandMessageConverter.class);
	}
	
	
	public <T> Class<?> findOutputClass(SearchCriteria<T> criteria, Class<?> referredClass) {
		if(criteria.getProjectCriteria() != null && StringUtils.isNotBlank(criteria.getProjectCriteria().getAlias())) {
			return getDomainConfigBuilder().getModel(criteria.getProjectCriteria().getAlias()).getReferredClass();
		}
		else if(criteria.getAggregateCriteria() != null ) {
			com.anthem.oss.nimbus.core.domain.model.state.repo.ModelRepository.Aggregation aggByAlias = 
					com.anthem.oss.nimbus.core.domain.model.state.repo.ModelRepository.Aggregation.getByAlias(criteria.getAggregateCriteria());
			
			Assert.notNull(aggByAlias, "Aggregation constant not found for the alias: " +criteria.getAggregateCriteria());
			
			return aggByAlias.getType();
		}
		
		return referredClass;
	}



	
}
