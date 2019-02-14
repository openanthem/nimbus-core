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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.SpringDataMongodbQuery;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.internal.AbstractListPaginatedParam.PageWrapper.PageRequestAndRespone;
import com.antheminc.oss.nimbus.support.EnableAPIMetricCollection;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.mongodb.AbstractMongodbQuery;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@EnableAPIMetricCollection
@SuppressWarnings({ "rawtypes", "unchecked"})
public class MongoSearchByQuery extends MongoDBSearch {

	private static final ScriptEngine groovyEngine = new ScriptEngineManager().getEngineByName("groovy");
	private static final String orderByAliasSuffix = ".";
	
	private static final String AGGREGATION_QUERY_REGEX = ".*\"?'?aggregate\"?'?\\s*:.*";
	private static final Pattern AGGREGATION_QUERY_REGEX_PATTERN = Pattern.compile(AGGREGATION_QUERY_REGEX, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

	
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
			
	        Predicate predicate = evaluate(referredClass, alias, criteria);
	        
			query.where(predicate);
			
			return this;
		}

		public QueryBuilder buildOrderBy(String criteria, Class<?> referredClass, String alias ) {
			
			if(StringUtils.isBlank(criteria) || !StringUtils.startsWith(criteria, alias+orderByAliasSuffix)) {
				return this;
			}
			
			OrderSpecifier orderBy = evaluate(referredClass, alias, criteria);
			
			if(orderBy != null)
				query.orderBy(orderBy);
			
			return this;
		}

		private <T> T evaluate(Class<?> referredClass, String alias, String criteria) {
			try {
				
				EntityPath<?> qInstance = SimpleEntityPathResolver.INSTANCE.createPath(referredClass);
				
				Bindings b = groovyEngine.createBindings();
				b.put(alias, qInstance);
				
				return (T)groovyEngine.eval(criteria, b);
				
			} catch (Exception ex) {
				throw new FrameworkRuntimeException("Cannot instantiate queryDsl class for entity: "+referredClass+ " "
						+ "please make sure the entity has been annotated with either @Domain or @Model and a Q Class has been generated for it", ex);	
			}
		}
	}
	
	@Override
	public <T> Object search(Class<T> referredClass, String alias, SearchCriteria<?> criteria) {
		String where = (String) criteria.getWhere();
		if(StringUtils.isNotBlank(where) && AGGREGATION_QUERY_REGEX_PATTERN.matcher(where).matches()) {
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
		Document query = Document.parse(cr);
		List<Document> pipeline = (List<Document>)query.get(Constants.SEARCH_REQ_AGGREGATE_PIPELINE.code);
		String aggregateCollection = query.getString(Constants.SEARCH_REQ_AGGREGATE_MARKER.code);
		List<Document> result = new ArrayList<Document>();
		getMongoOps().getCollection(aggregateCollection).aggregate(pipeline).iterator().forEachRemaining(a -> result.add(a));
		if(CollectionUtils.isNotEmpty(result)) {
			GenericType gt = getMongoOps().getConverter().read(GenericType.class, new org.bson.Document(GenericType.CONTENT_KEY, result));
			output.addAll(gt.getContent());
		}
		return output;
	}
	
	@Getter @Setter
	static class GenericType<T> {
		public static final String CONTENT_KEY = "content";
		
		List<T> content;
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