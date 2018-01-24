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

import static org.springframework.data.mongodb.core.aggregation.Aggregation.count;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.graphLookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.support.SpringDataMongodbQuery;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.CollectionUtils;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.entity.EntityAssociation;
import com.antheminc.oss.nimbus.entity.SearchCriteria;
import com.antheminc.oss.nimbus.entity.queue.Queue;
import com.antheminc.oss.nimbus.entity.user.GroupUser;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 * @author Rakesh Patel
 *
 */
public class MongoSearchByQuery extends MongoDBSearch {

	private static JustLogit logIt = new JustLogit(MongoSearchByQuery.class);
	
	public MongoSearchByQuery(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}


	@Override
	public <T> Object search(Class<?> referredClass, String alias, SearchCriteria<T> criteria) {
		if(StringUtils.contains((String)criteria.getWhere(),"aggregate")) {
			return searchByAggregation(referredClass, alias, criteria);
		}
		return searchByQuery(referredClass, alias, criteria);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private <T> Object searchByQuery(Class<?> referredClass, String alias, SearchCriteria<T> criteria) {
		
		Class<?> outputClass = findOutputClass(criteria, referredClass);
		
		SpringDataMongodbQuery<?> query = new SpringDataMongodbQuery<>(getMongoOps(), outputClass, alias);
		
		Predicate predicate = buildPredicate((String)criteria.getWhere(), referredClass, alias);
		
		OrderSpecifier orderBy = buildOrderSpecifier((String)criteria.getOrderby(), referredClass, alias);
		if(StringUtils.equalsIgnoreCase(criteria.getAggregateCriteria(), "count")) {
			//Holder<Long> holder = new Holder<>();
			//holder.setState(query.where(predicate).fetchCount());
			//return holder; // TODO refactor to support other single value aggregations ...
			return (Long)query.where(predicate).fetchCount();
		}
		
		if(criteria.getProjectCriteria() != null && !MapUtils.isEmpty(criteria.getProjectCriteria().getMapsTo())) {
			Collection<String> fields = criteria.getProjectCriteria().getMapsTo().values();
			List<PathBuilder> paths = new ArrayList<>();
			fields.forEach((f)->paths.add(new PathBuilder(referredClass, f)));
			if(orderBy!=null) {
				return query.where(predicate).orderBy(orderBy).fetch(paths.toArray(new PathBuilder[paths.size()]));
			}
			return query.where(predicate).fetch(paths.toArray(new PathBuilder[paths.size()]));
			
		}
		if(orderBy!=null) {
			return query.where(predicate).orderBy(orderBy).fetch();
		}
		
		if(StringUtils.equals(criteria.getFetch(),"1")) {
			return query.where(predicate).fetchOne();
		}
		List<?> response = query.where(predicate).fetch();
		
		return response;
		
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
	
	@SuppressWarnings("rawtypes")
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
		
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private  <T> Object searchByAggregation(Class<?> referredClass, String alias, SearchCriteria<T> criteria) {
//		Class<?> outputClass = findOutputClass(criteria, referredClass);
		
//		
//		List<?> result = new ArrayList<>();
//		EntityAssociation assoc = getConverter().convert(EntityAssociation.class, (String) criteria.getWhere());
//		
//		for(EntityAssociation entityAssociation: assoc.getAssociatedEntities()) {
//			List<AggregationOperation> aggregateOps = new ArrayList<>();
//
//			buildAggregationQuery(entityAssociation, aggregateOps);
//			
//			if(StringUtils.equalsIgnoreCase(criteria.getAggregateCriteria(), "count")) {
//				AggregationOperation countOp = count().as("state"); // TODO refactor to support other single value aggregations ...
//				aggregateOps.add(countOp);
//			}
//			
//			if(criteria.getProjectCriteria() != null && !MapUtils.isEmpty(criteria.getProjectCriteria().getMapsTo())) { 
//				String[] projectionFields = (String[])criteria.getProjectCriteria().getMapsTo().values().toArray();
//				AggregationOperation projectOps = Aggregation.project(projectionFields);
//				aggregateOps.add(projectOps);
//			}
//			
//			AggregationResults models = getMongoOps().aggregate(Aggregation.newAggregation(aggregateOps), assoc.getDomainAlias(), outputClass);
//			result.addAll(models.getMappedResults());
//		}
		
		//TODO - to be refactored - added null checks for LTSS deployment
		List<?> output = new ArrayList();
		String[] aggregationCriteria = StringUtils.split((String)criteria.getWhere(), "~~");
		Arrays.asList(aggregationCriteria).forEach((cr) -> {
			CommandResult commndResult = getMongoOps().executeCommand(cr);
			System.out.println("&&& Aggregation Query: "+cr+" --- Result: "+commndResult);
			if(commndResult != null && commndResult.get("result") != null && commndResult.get("result") instanceof BasicDBList) {
				BasicDBList result = (BasicDBList)commndResult.get("result");
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
	
	private void buildAggregationQuery(EntityAssociation assoc, List<AggregationOperation> aggregateOps) {
		buildAggregationPipeline(assoc, aggregateOps);
		
		if(!CollectionUtils.isEmpty(assoc.getAssociatedEntities())) {
			buildAggregationQueryNested(assoc.getAssociatedEntities(), aggregateOps);
		}
	}
	
	private void buildAggregationPipeline(EntityAssociation assoc, List<AggregationOperation> aggregateOps) {
		aggregateOps.add(buildGraphOperation(assoc));
		aggregateOps.add(match(Criteria.where(assoc.getAssociationAlias()).ne(Collections.EMPTY_LIST)));
		if(assoc.isUnwind()) {
			aggregateOps.add(unwind(assoc.getAssociationAlias()));
		}
	}
	
	private void buildAggregationQueryNested(List<EntityAssociation> associatedEntities, List<AggregationOperation> aggregateOps) {
		associatedEntities.forEach((assoc) -> {
			buildAggregationPipeline(assoc, aggregateOps);
			
			if(!CollectionUtils.isEmpty(assoc.getAssociatedEntities())) {
				buildAggregationQueryNested(assoc.getAssociatedEntities(), aggregateOps);
			}
		});
	}
	
	private AggregationOperation buildGraphOperation(EntityAssociation assoc) {
		if(!CollectionUtils.isEmpty(assoc.getCriteria())) {
			return graphLookup(assoc.getDomainAlias())
					.startWith(assoc.getAssociationStartWith())
					.connectFrom(assoc.getAssociationFrom())
					.connectTo(assoc.getAssociationTo())
					.restrict(assoc.getCriteria().stream() //TODO chaining the criteria if more than one
							.map((restriction) -> Criteria.where(restriction.getKey()).is(restriction.getValue()))
							.findFirst().get())
					.as(assoc.getAssociationAlias());
		}
		else {
			return graphLookup(assoc.getDomainAlias())
					.startWith(assoc.getAssociationStartWith())
					.connectFrom(assoc.getAssociationFrom())
					.connectTo(assoc.getAssociationTo())
					.as(assoc.getAssociationAlias());
		}
	}
	
}
