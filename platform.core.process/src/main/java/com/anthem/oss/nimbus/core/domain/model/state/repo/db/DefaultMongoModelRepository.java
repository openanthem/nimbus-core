/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.definition.SearchNature.StartsWith;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.repo.IdSequenceRepository;
import com.anthem.oss.nimbus.core.util.ClassLoadUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter 
public class DefaultMongoModelRepository implements ModelRepository {

	MongoOperations mongoOps;
	
	IdSequenceRepository idSequenceRepo;
	
	public DefaultMongoModelRepository(MongoOperations mongoOps, IdSequenceRepository idSequenceRepo) {
		this.mongoOps = mongoOps;
		this.idSequenceRepo = idSequenceRepo;
	}
	
	@Override
	public <T> T _new(Class<T> referredClass, String alias) {
		T input = ClassLoadUtils.newInstance(referredClass);
		return _new(referredClass, alias, input);
	}

	@Override
	public <T> T _new(Class<T> referredClass, String alias, T input) {
		PropertyAccessor inputAccessor = PropertyAccessorFactory.forBeanPropertyAccess(input);
		if(inputAccessor.getPropertyType("id") == Long.class) {
			inputAccessor.setPropertyValue("id", idSequenceRepo.getNextSequenceId("global"));
		}
		mongoOps.insert(input, alias);
		return input;
	}

	@Override
	public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias) {
		T state = mongoOps.findById(id, referredClass, alias);
		return state;
	}
	
	@Override
	public <ID extends Serializable, T> T _update(String alias, ID id, String path, T state) {
	 
	  Query query = new Query(Criteria.where("_id").is(id));
	  Update update = new Update();
	  if(StringUtils.isBlank(path) || StringUtils.equalsIgnoreCase(path, "/c")) {
		  mongoOps.save(state, alias);
	  }
	  else{
		  if(StringUtils.equals(path, "/id") || StringUtils.equals(path, "id")) { 
		  	// if we updated the  document with path "/id", MongoDB is upserting with a new document with same _id but property field as "/id". e.g. if patient document already exist with
		  	// all the fields populated, it would insert a new patient document with same _id like:
		  	//	{"_id": NumberLong(1), "/id":NumberLong(1)}
		  	// whereas there is already a correct patient document as:
		  	//	{"_id": NumberLong(1), "firstName":"Rakesh"}
		  	// I think this is because the "id" property gets saved in the monog as "_id" key and so when the next update comes with path="/id", for MongoDB, it would be a new field (non id),
		  	// hence, ends up creating a new document. for now just returning from this mehtod without going to MongoDB.
		  		return state;
		  	}
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
