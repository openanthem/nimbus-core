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
package com.antheminc.oss.nimbus.domain.model.state.repo.db;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.repository.support.SpringDataMongodbQuery;
import org.springframework.data.repository.support.PageableExecutionUtils;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.entity.SearchCriteria;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.mongodb.AbstractMongodbQuery;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * @author Rakesh Patel
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked"})
public class MongoSearchByQuery extends MongoDBSearch {

	private static JustLogit logIt = new JustLogit(MongoSearchByQuery.class);
	
	public MongoSearchByQuery(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}


	@Override
	public <T> Object search(Class<?> referredClass, String alias, SearchCriteria<T> criteria) {
		if(StringUtils.contains((String)criteria.getWhere(),Constants.SEARCH_REQ_AGGREGATE_MARKER.code)) {
			return searchByAggregation(referredClass, alias, criteria);
		}
		return searchByQuery(referredClass, alias, criteria);
	}
	
	
	private <T> Object searchByQuery(Class<?> referredClass, String alias, SearchCriteria<T> criteria) {
		Class<?> outputClass = findOutputClass(criteria, referredClass);
		
		AbstractMongodbQuery query = new SpringDataMongodbQuery<>(getMongoOps(), outputClass, alias);
		
		Predicate predicate = buildPredicate((String)criteria.getWhere(), referredClass, alias);
		
		query = buildQueryPredicate(query, predicate);
		
		query = buildQueryOrderBy(query, (String)criteria.getOrderby(), referredClass, alias);
		
		if(StringUtils.equalsIgnoreCase(criteria.getAggregateCriteria(), Constants.SEARCH_REQ_AGGREGATE_COUNT.code)) {
			return (Long)query.fetchCount();
		}
		
		if(StringUtils.isNotBlank(criteria.getFetch())) {
			return query.where(predicate).fetchOne();
		}
		
		if(criteria.getProjectCriteria() != null && !MapUtils.isEmpty(criteria.getProjectCriteria().getMapsTo())) {
			return searchWithProjection(referredClass, criteria, query);
			
		}
		
		if(criteria.getPageRequest() != null) {
			return findAllPageable(referredClass, alias, criteria.getPageRequest(), query);
		}
		
		List<?> response = query.fetch();
		return response;
		
	}


	private AbstractMongodbQuery buildQueryPredicate(AbstractMongodbQuery query, Predicate predicate) {
		query = query.where(predicate);
		return query;
	}
	
	private AbstractMongodbQuery buildQueryOrderBy(AbstractMongodbQuery query, String criteria, Class<?> referredClass, String alias ) {
		OrderSpecifier orderBy = buildOrderSpecifier(criteria, referredClass, alias);
		if(orderBy != null)
			query = query.orderBy(orderBy);
		return query;
	}
	
	private <T> Object findAllPageable(Class<?> referredClass, String alias, Pageable pageRequest, AbstractMongodbQuery query) {
		AbstractMongodbQuery qPage = query.offset(pageRequest.getOffset()).limit(pageRequest.getPageSize());
		
		if(pageRequest.getSort() != null){
			PathBuilder<?> entityPath = new PathBuilder(referredClass, alias);
			for (Order order : pageRequest.getSort()) {
			    PathBuilder<Object> path = entityPath.get(order.getProperty());
			    qPage.orderBy(new OrderSpecifier(com.querydsl.core.types.Order.valueOf(order.getDirection().name().toUpperCase()), path));
			}
		}
		return PageableExecutionUtils.getPage(qPage.fetchResults().getResults(), pageRequest, () -> query.fetchCount());
	}
	
	private <T> Object searchWithProjection(Class<?> referredClass, SearchCriteria<T> criteria, AbstractMongodbQuery query) {
		Collection<String> fields = criteria.getProjectCriteria().getMapsTo().values();
		List<PathBuilder> paths = new ArrayList<>();
		fields.forEach((f)->paths.add(new PathBuilder(referredClass, f)));
		return query.fetch(paths.toArray(new PathBuilder[paths.size()]));
	}

	private Predicate buildPredicate(String criteria, Class<?> referredClass, String alias) {
		if(StringUtils.isBlank(criteria)) {
			return null;
		}
		
		String groovyScript = criteria.toString().replaceAll("<", "(").replace(">", ")");
				
		final Binding binding = new Binding();
		Object obj = createQueryDslClassInstance(referredClass);
        binding.setProperty(alias, obj);
        
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
        localDateTime.atZone(TimeZone.getDefault().toZoneId());
       
        binding.setProperty("todaydate",localDateTime);
        final GroovyShell shell = new GroovyShell(binding); 
        return (Predicate)shell.evaluate(groovyScript);
	}
	
	private OrderSpecifier buildOrderSpecifier(String criteria, Class<?> referredClass, String alias) {
		if(StringUtils.isBlank(criteria)) {
			return null;
		}
		
		String groovyScript = criteria.toString().replaceAll("<", "(").replace(">", ")");
				
		final Binding binding = new Binding();
		Object obj = createQueryDslClassInstance(referredClass);
        binding.setProperty(alias, obj);
        
        final GroovyShell shell = new GroovyShell(binding); 
        return (OrderSpecifier)shell.evaluate(groovyScript);
	}
	
	private Object createQueryDslClassInstance(Class<?> referredClass) {
		Object obj = null;
		try {
			String cannonicalQuerydslclass = referredClass.getCanonicalName().replace(referredClass.getSimpleName(), "Q".concat(referredClass.getSimpleName()));
			Class<?> cl = Class.forName(cannonicalQuerydslclass);
			Constructor<?> con = cl.getConstructor(String.class);
			obj = con.newInstance(referredClass.getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
		
	private  <T> Object searchByAggregation(Class<?> referredClass, String alias, SearchCriteria<T> criteria) {
		List<?> output = new ArrayList();
		String[] aggregationCriteria = StringUtils.split((String)criteria.getWhere(), Constants.SEARCH_NAMED_QUERY_DELIMTER.code);
		Arrays.asList(aggregationCriteria).forEach((cr) -> {
			CommandResult commndResult = getMongoOps().executeCommand(cr);
			logIt.trace(()-> "&&& Aggregation Query: "+cr+" --- Result: "+commndResult);
			if(commndResult != null && commndResult.get(Constants.SEARCH_NAMED_QUERY_RESULT.code) != null && commndResult.get(Constants.SEARCH_NAMED_QUERY_RESULT.code) instanceof BasicDBList) {
				BasicDBList result = (BasicDBList)commndResult.get(Constants.SEARCH_NAMED_QUERY_RESULT.code);
				if(criteria.getProjectCriteria() != null && StringUtils.isNotBlank(criteria.getProjectCriteria().getAlias())) {
					BasicDBObject dbObject = (BasicDBObject)result.get(0);
					if(dbObject != null && dbObject.get(criteria.getProjectCriteria().getAlias()) instanceof BasicDBList) {
						result = (BasicDBList)dbObject.get(criteria.getProjectCriteria().getAlias());
					}
				}
				output.addAll(getMongoOps().getConverter().read(List.class, result));
			}
		});
		return output;
	}
	
}
