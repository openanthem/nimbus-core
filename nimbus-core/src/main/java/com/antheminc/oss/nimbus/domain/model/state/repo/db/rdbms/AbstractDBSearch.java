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
package com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository.Aggregation;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.DBSearchOperation;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public abstract class AbstractDBSearch implements DBSearchOperation {

	private final DomainConfigBuilder domainConfigBuilder;
	
	private final BeanResolverStrategy beanResolver;
	
	public AbstractDBSearch(BeanResolverStrategy beanResolver) {
		this.beanResolver = beanResolver;
		this.domainConfigBuilder = beanResolver.get(DomainConfigBuilder.class);
	}
	
	// same as in MongoDBSearch.java - can be abstracted
	public <T> Class<?> findOutputClass(SearchCriteria<?> criteria, Class<T> referredClass) {
		if(criteria.getProjectCriteria() != null && StringUtils.isNotBlank(criteria.getProjectCriteria().getAlias())) {
			return getDomainConfigBuilder().getModel(criteria.getProjectCriteria().getAlias()).getReferredClass();
		}
		else if(criteria.getAggregateCriteria() != null ) {
			Aggregation aggByAlias = Aggregation.getByAlias(criteria.getAggregateCriteria());
			
			Assert.notNull(aggByAlias, "Aggregation constant not found for the alias: " +criteria.getAggregateCriteria());
			
			return aggByAlias.getType();
		}
		
		return referredClass;
	}

}
