/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.*;

import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param;
import com.anthem.oss.nimbus.core.domain.definition.SearchNature.StartsWith;
import com.anthem.oss.nimbus.core.util.ClassLoadUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Component("rep_mongodb")
@Getter @Setter 
public class DefaultMongoModelRepository implements ModelRepository {

	@Autowired MongoOperations mongoOps;
	
	@Override
	public <T> T _new(Class<T> referredClass, String alias) {
		T input = ClassLoadUtils.newInstance(referredClass);
		return _new(referredClass, alias, input);
	}

	@Override
	public <T> T _new(Class<T> referredClass, String alias, T input) {
		mongoOps.insert(input, alias);
		return input;
	}

	@Override
	public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias) {
		T state = mongoOps.findById(id, referredClass, alias);
		return state;
	}
	
	@Override
	 
	 public <T> T _update(String alias, String id, String path, T state) {
	 
	  Query query = new Query(Criteria.where("_id").is(id));
	  Update update = new Update();
	  if(StringUtils.isBlank(path)) {
		  mongoOps.save(state, alias);
	  }
	  else{
		   path = StringUtils.substringAfter(path, "/");
		   path = path.replaceAll("/", "\\.");
		   if(state == null)
			   update.unset(path);
		   else
			   update.set(path, state);
		   mongoOps.upsert(query, update, alias);
	  } 
	  return state;
	 }
	 

	
	@Override
	public <T> T _replace(String alias, T state) {
		mongoOps.save(state, alias);
		return state;
	}

	@Override
	public void _replace(Param<?> param) {

	}

	@Override
	public void _replace(List<Param<?>> params) {

	}
	
	@Override
	public <ID extends Serializable, T> T _delete(ID id, Class<T> referredClass, String alias) {
		Query query = new Query(Criteria.where("_id").is(id));
		T state = mongoOps.findAndRemove(query, referredClass, alias);
		return state;
	}
	
	@Override
	public <T, C> List<T> _search(Class<T> referredClass, String alias, C criteria) {
		
		if(criteria==null) {
			return mongoOps.findAll(referredClass, alias);
		}
		
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("version");
		for (Field field : referredClass.getDeclaredFields()) {
			if (field.getType().isAssignableFrom(String.class)) {
				field.setAccessible(true);
				try {
					String checkString = (String) FieldUtils.readField(field, criteria);
					if (checkString != null && checkString.isEmpty())
						matcher = matcher.withIgnorePaths(field.getName());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			if (field.isAnnotationPresent(StartsWith.class))
				matcher = matcher.withMatcher(field.getName(), startsWith());
		}
		
		Example<C> example =  Example.of(criteria, matcher);
		Criteria c = Criteria.byExample(example);
		Query query = new Query(c);
		return mongoOps.find(query, referredClass, alias);
	}
	
}
