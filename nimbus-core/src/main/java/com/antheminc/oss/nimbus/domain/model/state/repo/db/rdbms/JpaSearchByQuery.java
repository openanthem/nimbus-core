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
package com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria.LookupSearchCriteria;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria.QuerySearchCriteria;
import com.antheminc.oss.nimbus.support.EnableAPIMetricCollection;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

/**
 * @author Soham.Chakravarti
 *
 */
@EnableAPIMetricCollection
public class JpaSearchByQuery extends AbstractDBSearchByQuery {

	//private EntityManagerFactory entityManagerFactory;
	@Autowired
	@PersistenceContext
	private EntityManager em;
	
	public JpaSearchByQuery(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		//this.entityManagerFactory = entityManagerFactory;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> QueryDslJpaRepositoryExtension<T, ?> buildQueryDslExecutor(EntityManager em, Class<T> referredClass) {
		JpaEntityInformation<T, ?> info = JpaEntityInformationSupport.getEntityInformation(referredClass, em);
		
		return new QueryDslJpaRepositoryExtension(info, em);
	}
	
	@Override
	public boolean shouldAllow(SearchCriteria<?> criteria) {
		return criteria instanceof QuerySearchCriteria || criteria instanceof LookupSearchCriteria;
	}
	
	@Override
	public <T> Object search(Class<T> referredClass, String alias, SearchCriteria<?> criteria) {
		QueryDslJpaRepositoryExtension<T, ?> queryDslExecutor = buildQueryDslExecutor(em, referredClass);
		Predicate predicate = evaluate(referredClass, alias, (String)criteria.getWhere());
		
		if(StringUtils.equalsIgnoreCase(criteria.getAggregateCriteria(), Constants.SEARCH_REQ_AGGREGATE_COUNT.code)) {
			return queryDslExecutor.count(predicate);
		}
		
		// TODO: Use outputClass to build a result of different type than being used to search on
		Class<?> outputClass = findOutputClass(criteria, referredClass);
		
		PathBuilder<?>[] projectionPaths = buildProjectionPathBuilder(referredClass, criteria);
		OrderSpecifier<?> orderBy = evaluate(referredClass, alias, (String)criteria.getOrderby());

		if(StringUtils.isNotBlank(criteria.getFetch()))
			return queryDslExecutor.findOne(predicate, outputClass, projectionPaths, orderBy);

		else if(criteria.getPageRequest() != null) 
			return queryDslExecutor.findAll(predicate, outputClass, criteria.getPageRequest(), projectionPaths, orderBy);
		
		else
			return queryDslExecutor.findAll(predicate, outputClass, projectionPaths, orderBy);
	
	}
}
