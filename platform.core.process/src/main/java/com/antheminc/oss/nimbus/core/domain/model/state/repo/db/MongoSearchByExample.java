package com.antheminc.oss.nimbus.core.domain.model.state.repo.db;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

import java.lang.reflect.Field;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.ReflectionUtils;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.core.domain.definition.SearchNature.StartsWith;
import com.antheminc.oss.nimbus.core.entity.SearchCriteria;

/**
 * @author Rakesh Patel
 *
 */ 
public class MongoSearchByExample extends MongoDBSearch {

	public MongoSearchByExample(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	@Override
	public <T> Object search(Class<?> referredClass, String alias, SearchCriteria<T> criteria) {
		Query query = buildQuery(referredClass, alias, criteria.getWhere());
		
		if(StringUtils.equalsIgnoreCase(criteria.getAggregateCriteria(), "count")){
			return getMongoOps().count(query, referredClass, alias); // TODO refactor to support other single value aggregations ...
		}
		if(criteria.getProjectCriteria() != null && StringUtils.isNotBlank(criteria.getProjectCriteria().getAlias())) {
			referredClass = findOutputClass(criteria, referredClass);
			return getMongoOps().find(query, referredClass, alias);
		}
		
		return getMongoOps().find(query, referredClass, alias);
	}
	
	private <T> Query buildQuery(Class<?> referredClass, String alias, T criteria) {
		if(criteria == null) 
			return new Query();
		
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withIgnoreNullValues().withIgnorePaths("version");
		
		matcher = recurseAllFieldsAndBuildMatcher(referredClass, criteria, matcher);
		
		Example<T> example =  Example.of(criteria, matcher);
		Criteria c = Criteria.byExample(example);
		Query query = new Query(c);
		return query;
	}

	// TODO - recursive matcher is not building correctly - the fieldName should be "." seperated not just the current field name. 
	// e.g. CMCase > Patient > firstName ==> should be built as "patientReferred.firstName", the commented code only gets the fieldName as "firstName" with recursion into patient-- need to fix
	private <T> ExampleMatcher recurseAllFieldsAndBuildMatcher(Class<?> referredClass, T criteria, ExampleMatcher matcher) {
		for (Field field : FieldUtils.getAllFieldsList(referredClass)) {
			field.setAccessible(true);
			if (field.getType().isAssignableFrom(String.class)) {
				
				try {
					String checkString = (String) FieldUtils.readField(field, criteria);
					if (checkString != null && checkString.isEmpty())
						matcher = matcher.withIgnorePaths(field.getName());
				} catch (IllegalAccessException e) {
					throw new FrameworkRuntimeException(e);
				}
			}
			
//			if(!field.getType().isPrimitive() && criteria != null) {
//				try {
//					recurseAllFieldsAndBuildMatcher(field.getType(), FieldUtils.readField(field, criteria), matcher);
//				} catch (Exception e) {
//					throw new FrameworkRuntimeException("Could not read value of field: "+field+" on object: "+criteria, e);
//				}
//			}
			
			if (field.isAnnotationPresent(StartsWith.class))
				matcher = matcher.withMatcher(field.getName(), startsWith());
		}
		return matcher;
	}
	
}
