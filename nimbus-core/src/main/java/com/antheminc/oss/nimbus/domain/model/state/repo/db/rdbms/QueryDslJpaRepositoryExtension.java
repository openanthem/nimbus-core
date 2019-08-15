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

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QSort;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.util.Assert;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.model.state.internal.AbstractListPaginatedParam.PageWrapper.PageRequestAndRespone;
import com.antheminc.oss.nimbus.support.pojo.ClassLoadUtils;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;


/**
 * @author Soham.Chakravarti
 *
 */
public class QueryDslJpaRepositoryExtension<T, ID extends Serializable> extends QuerydslJpaRepository<T, ID> {

    //All instance variables are available in super, but they are private
    private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;

    private final EntityPath<T> path;
    private final PathBuilder<T> builder;
    private final Querydsl querydsl;
    
    
	public QueryDslJpaRepositoryExtension(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
		this(entityInformation, entityManager, DEFAULT_ENTITY_PATH_RESOLVER);
	}

	public QueryDslJpaRepositoryExtension(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager, EntityPathResolver resolver) {
		super(entityInformation, entityManager);
        this.path = resolver.createPath(entityInformation.getJavaType());
        this.builder = new PathBuilder<T>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
	}
	
//	public Page<T> findAll(Predicate predicate, Pageable pageable, FactoryExpression<T> factoryExpression) {
//		return PageableExecutionUtils.getPage(query.fetch(), pageable, countQuery::fetchCount);
//	}
	
    public PageRequestAndRespone<?> findAll(Predicate predicate, Class<?> outputClass, Pageable pageable, PathBuilder<?>[] projectionPaths, OrderSpecifier<?>...orderBy) {
        Assert.notNull(pageable, "Pageable must not be null!");

		final JPQLQuery<?> countQuery = createCountQuery(predicate);
		final JPQLQuery<?> query = querydsl.applyPagination(pageable, createQuery(predicate, projectionPaths));
		
		if(ArrayUtils.isNotEmpty(orderBy)) 
			query.orderBy(orderBy);
		
		List<?> results = query.fetch();
		List<?> convertedResults = applyPrimitiveConversionToList(outputClass, results);
		
		return new PageRequestAndRespone<>(convertedResults, pageable, countQuery::fetchCount);
    }

    public List<?> findAll(Predicate predicate, Class<?> outputClass, PathBuilder<?>[] projectionPaths, OrderSpecifier<?> orderBy) {
    	final JPQLQuery<?> query = createQuery(predicate, projectionPaths);
    	
    	final List<?> results;
    	if(orderBy==null) 
    		results = query.fetch();
    	else
    		results = querydsl.applySorting(new QSort(orderBy), query).fetch();
    	
    	return applyPrimitiveConversionToList(outputClass, results);
    }
    

    public Object findOne(Predicate predicate, Class<?> outputClass, PathBuilder<?>[] projectionPaths, OrderSpecifier<?> orderBy) {
    	final JPQLQuery<?> query = createQuery(predicate, projectionPaths);
    	
    	final Object result;
    	if(orderBy==null) 
    		result = query.fetchOne();
    	else
    		result = querydsl.applySorting(new QSort(orderBy), query).fetchOne();
    	
    	return applyPrimitiveConversionToOne(outputClass, result);
    }
    
    
    protected List<?> applyPrimitiveConversionToList(Class<?> outputClass, List<?> results) {
    	if(!ClassLoadUtils.isPrimitive(outputClass) || CollectionUtils.isEmpty(results))
    		return results;
    	
    	return results.stream()
				.map(row->applyPrimitiveConversionToOne(outputClass, row))
				.collect(Collectors.toList());
    }
    
    protected Object applyPrimitiveConversionToOne(Class<?> outputClass, Object result) {
		if(ClassLoadUtils.isPrimitive(outputClass) && Tuple.class.isInstance(result)) {
			Tuple tuple = Tuple.class.cast(result);
			
			if(tuple.size()!=1)
				throw new InvalidConfigException("Projection alias is of primitive type: "+outputClass+" but query returned more than one column-cell");
			
			return tuple.get(0, outputClass);
			
		} else
			return result;
    }
    
    private JPQLQuery<?> createQuery(Predicate predicate, PathBuilder<?>[] projectionPaths) {
    	return ArrayUtils.isEmpty(projectionPaths) ? createQuery(predicate) : createQuery(predicate).select(projectionPaths);
    }
}
