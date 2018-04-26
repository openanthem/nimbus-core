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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.SpringDataMongodbQuery;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.internal.AbstractListPaginatedParam.PageWrapper.PageRequestAndRespone;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.mongodb.BasicDBList;
import com.mongodb.CommandResult;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.mongodb.AbstractMongodbQuery;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.RequiredArgsConstructor;

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
	
	@RequiredArgsConstructor
	class QueryBuilder {
		
		private final AbstractMongodbQuery query;
		
		public QueryBuilder(MongoOperations mongoOps, Class<?> clazz, String collectionName) {
			query = new SpringDataMongodbQuery<>(mongoOps, clazz, collectionName);
		}
		
		public AbstractMongodbQuery get() {
			return query;
		}
		
		public QueryBuilder buildPredicate(String criteria, Class<?> referredClass, String alias ) {
			
			if(StringUtils.isBlank(criteria)) {
				return this;
			}
			
			final GroovyShell shell = createQBinding(referredClass, alias); 
	        Predicate predicate = (Predicate)shell.evaluate(criteria);
	        
			query.where(predicate);
			
			return this;
		}

		public QueryBuilder buildOrderBy(String criteria, Class<?> referredClass, String alias ) {
			
			if(StringUtils.isBlank(criteria)) {
				return this;
			}
			
			final GroovyShell shell = createQBinding(referredClass, alias); 
	        OrderSpecifier orderBy = (OrderSpecifier)shell.evaluate(criteria);
	        
			if(orderBy != null)
				query.orderBy(orderBy);
			
			return this;
		}
		
		private GroovyShell createQBinding(Class<?> referredClass, String alias) {
			final Binding binding = new Binding();
			Object obj = createQueryDslClassInstance(referredClass);
	        binding.setProperty(alias, obj);
	        
	        final GroovyShell shell = new GroovyShell(referredClass.getClassLoader(), binding);
			return shell;
		}
		
		private Object createQueryDslClassInstance(Class<?> referredClass) {
			Object obj = null;
			try {
				String cannonicalQuerydslclass = referredClass.getCanonicalName().replace(referredClass.getSimpleName(), "Q".concat(referredClass.getSimpleName()));
				Class<?> cl = Class.forName(cannonicalQuerydslclass);
				Constructor<?> con = cl.getConstructor(String.class);
				obj = con.newInstance(referredClass.getSimpleName());
			} catch (Exception e) {
				throw new FrameworkRuntimeException("Cannot instantiate queryDsl class for entity: "+referredClass+ " "
						+ "please make sure the entity has been annotated with either @Domain or @Model and a Q Class has been generated for it", e);
			}
			return obj;
		}
		
	}
	
	@Override
	public <T> Object search(Class<T> referredClass, String alias, SearchCriteria<?> criteria) {
		if(StringUtils.contains((String)criteria.getWhere(),Constants.SEARCH_REQ_AGGREGATE_MARKER.code)) {
			return searchByAggregation(referredClass, alias, criteria);
		}
		return searchByQuery(referredClass, alias, criteria);
	}
	
	
	private <T> Object searchByQuery(Class<?> referredClass, String alias, SearchCriteria<T> criteria) {
		Class<?> outputClass = findOutputClass(criteria, referredClass);
		
		AbstractMongodbQuery query = new QueryBuilder(getMongoOps(), outputClass, alias)
										.buildPredicate((String)criteria.getWhere(), referredClass, alias)
										.buildOrderBy((String)criteria.getOrderby(), referredClass, alias)
										.get();
		
		PathBuilder[] projectionPaths = buildProjectionPathBuilder(referredClass, criteria, query);
				
		if(StringUtils.equalsIgnoreCase(criteria.getAggregateCriteria(), Constants.SEARCH_REQ_AGGREGATE_COUNT.code)) {
			return (Long)query.fetchCount();
		}
		
		if(StringUtils.isNotBlank(criteria.getFetch())) {
			return query.fetchOne(projectionPaths);
		}

		if(criteria.getPageRequest() != null) {
			return findAllPageable(referredClass, alias, criteria.getPageRequest(), query, projectionPaths);
		}
		
		return query.fetch(projectionPaths);
		
	}

	private PageRequestAndRespone<Object> findAllPageable(Class<?> referredClass, String alias, Pageable pageRequest, AbstractMongodbQuery query, PathBuilder[] projectionPaths) {
		AbstractMongodbQuery qPage = query.offset(pageRequest.getOffset()).limit(pageRequest.getPageSize());
		
		if(pageRequest.getSort() != null){
			PathBuilder<?> entityPath = new PathBuilder(referredClass, alias);
			for (Order order : pageRequest.getSort()) {
			    PathBuilder<Object> path = entityPath.get(order.getProperty());
			    qPage.orderBy(new OrderSpecifier(com.querydsl.core.types.Order.valueOf(order.getDirection().name().toUpperCase()), path));
			}
		}
		return new PageRequestAndRespone<Object>(qPage.fetchResults(projectionPaths).getResults(), pageRequest, () -> query.fetchCount());
	}
	
	private PathBuilder[] buildProjectionPathBuilder(Class<?> referredClass, SearchCriteria criteria, AbstractMongodbQuery query) {
		List<PathBuilder> paths = new ArrayList<>();
		if(criteria.getProjectCriteria() != null && !MapUtils.isEmpty(criteria.getProjectCriteria().getMapsTo())) {
			Collection<String> fields = criteria.getProjectCriteria().getMapsTo().values();
			fields.forEach((f)->paths.add(new PathBuilder(referredClass, f)));
			return paths.toArray(new PathBuilder[paths.size()]);
		}
		return paths.toArray(new PathBuilder[paths.size()]);
	}
	
	private  <T> Object searchByAggregation(Class<?> referredClass, String alias, SearchCriteria<T> criteria) {
		List<?> output = new ArrayList();
		String cr = (String)criteria.getWhere();
		logIt.info(() -> "### Aggregation query: "+cr);
		
		long startTime = System.currentTimeMillis();
		CommandResult commndResult = getMongoOps().executeCommand(cr);
		long endTime = System.currentTimeMillis();
		logIt.info(() -> " took "+(endTime-startTime)+ " ms ");
				
		if (commndResult != null && commndResult.get(Constants.SEARCH_NAMED_QUERY_RESULT.code) instanceof BasicDBList) {
			BasicDBList result = (BasicDBList)commndResult.get(Constants.SEARCH_NAMED_QUERY_RESULT.code);
			output.addAll(getMongoOps().getConverter().read(List.class, result));
			logIt.info(() -> "with result size: "+output.size());
			logIt.trace(()-> " and result content: "+commndResult);
		}
		return output;
	}
	
	/*
	 * WIP: sorting and pagination for aggregate queries:
	 * 
	 * { $sort : { livingArrangement : -1,subscriberId: -1} },
	 * { $skip : 5 },
	 * { $limit : 5 }
	 *
	 */
//	private  <T> Object searchByAggregation1(Class<?> referredClass, String alias, SearchCriteria<T> criteria) {
//		List<?> output = new ArrayList();
//		String cr = (String)criteria.getWhere();
//		
//		criteria.setPageRequest(new PageRequest(0, 1, new Sort(Direction.ASC, "_id")));
//		
//		//if(criteria.getPageRequest() != null) {
//		int index = cr.lastIndexOf("]"); 
//		Integer count = executeCountQuery(cr, index); //TODO if count is null return error ?
//		
//		String pageQuery = new StringBuilder(cr).insert(index, addAggregatePageCriteria(criteria)).toString();
//		//}
//		CommandResult commndResult = getMongoOps().executeCommand(pageQuery);
//		logIt.info(()-> "&&& Aggregation Query: "+pageQuery+" --- Result: "+commndResult);
//		if (commndResult != null && commndResult.get(Constants.SEARCH_NAMED_QUERY_RESULT.code) != null
//				&& commndResult.get(Constants.SEARCH_NAMED_QUERY_RESULT.code) instanceof BasicDBList) {
//			BasicDBList result = (BasicDBList)commndResult.get(Constants.SEARCH_NAMED_QUERY_RESULT.code);
//			if(criteria.getProjectCriteria() != null && StringUtils.isNotBlank(criteria.getProjectCriteria().getAlias())) {
//				BasicDBObject dbObject = (BasicDBObject)result.get(0);
//				if(dbObject != null && dbObject.get(criteria.getProjectCriteria().getAlias()) instanceof BasicDBList) {
//					result = (BasicDBList)dbObject.get(criteria.getProjectCriteria().getAlias());
//				}
//			}
//			output.addAll(getMongoOps().getConverter().read(List.class, result));
//		}
//		
//		return output;
//	}
//
//
//	private Integer executeCountQuery(String cr, int index) {
//		String countQuery = new StringBuilder(cr).insert(index, addAggregateCountCriteria()).toString();
//		CommandResult commndResult1 = getMongoOps().executeCommand(countQuery);
//		//{ "result" : [ { "state" : 12}] , "ok" : 1.0}
//		if (commndResult1 != null && commndResult1.get(Constants.SEARCH_NAMED_QUERY_RESULT.code) != null
//				&& commndResult1.get(Constants.SEARCH_NAMED_QUERY_RESULT.code) instanceof BasicDBList) {
//			BasicDBList result1 = (BasicDBList)commndResult1.get(Constants.SEARCH_NAMED_QUERY_RESULT.code);
//			if(result1 != null && result1.size() > 0) {
//				Holder<Integer> count = getMongoOps().getConverter().read(Holder.class, ((BasicDBObject)result1.get(0)));
//				System.out.println("Total count: "+count);
//				return count.getState();
//			}
//		}
//		return null;
//	}
//	
//	private <T> String addAggregateCountCriteria() {
//		AggregatePageCriteriaBuilder builder = new AggregatePageCriteriaBuilder(new StringBuilder());
//		
//		return builder.count().build();
//	}
//	
//	private <T> String addAggregatePageCriteria(SearchCriteria<T> criteria) {
//		Pageable pageable = criteria.getPageRequest();
//		AggregatePageCriteriaBuilder builder = new AggregatePageCriteriaBuilder(new StringBuilder());
//		
//		return builder.sort(pageable.getSort())
//				.skip(pageable.getPageNumber() * pageable.getPageSize())
//				.limit(pageable.getPageSize())
//				.build();
//	}
//	
//	
//	class AggregatePageCriteriaBuilder<T> {
//		
//		private static final String PREFIX = ",{";
//		private static final String SUFFIX = "}";
//				
//		private static final String COUNT = PREFIX+" $count: \"state\" "+SUFFIX;
//		
//		private static final String SORT_PREFIX = PREFIX+" $sort: {";
//		private static final String SKIP_PREFIX = PREFIX+" $skip: ";
//		private static final String LIMIT_PREFIX = PREFIX+" $limit: ";
//		
//		
//		private StringBuilder pageBuilder;
//		
//		AggregatePageCriteriaBuilder(StringBuilder pageBuilder){
//			this.pageBuilder = pageBuilder;
//		}
//		
//		private String build() {
//			return pageBuilder.toString();
//		}
//		
//		private AggregatePageCriteriaBuilder count() {
//			pageBuilder.append(COUNT);
//			return this;
//		}
//		
//		private AggregatePageCriteriaBuilder sort(Sort sort) {
//			if(sort != null) {
//				pageBuilder.append(SORT_PREFIX);
//				
//				sort.forEach(o -> {
//					String key = o.getProperty();
//					int direction = o.getDirection() == Direction.ASC ? 1 : -1;
//					pageBuilder.append(key + " : " + direction +", ");
//				});
//				
//				pageBuilder.append(SUFFIX+SUFFIX);
//			}
//			return this;
//		}
//		
//		private AggregatePageCriteriaBuilder skip(int skip) {
//			 pageBuilder.append(SKIP_PREFIX)
//					.append(skip)
//					.append(SUFFIX);
//			 return this;
//		}
//		
//		private AggregatePageCriteriaBuilder limit(int limit) {
//			pageBuilder.append(LIMIT_PREFIX)
//					.append(limit)
//					.append(SUFFIX);
//			return this;
//		}
//	}
	
	
}
