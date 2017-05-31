///**
// * 
// */
//package com.anthem.oss.nimbus.core.domain.model.state.repo.db;
//
//import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;
//
//import java.io.Serializable;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Field;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.TimeZone;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.reflect.FieldUtils;
//import org.springframework.beans.PropertyAccessor;
//import org.springframework.beans.PropertyAccessorFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.ExampleMatcher;
//import org.springframework.data.mongodb.core.MongoOperations;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//
//import com.anthem.oss.nimbus.core.domain.definition.SearchNature.StartsWith;
//import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
//import com.anthem.oss.nimbus.core.domain.model.state.repo.IdSequenceRepository;
//import com.anthem.oss.nimbus.core.entity.SearchCriteria.ExampleSearchCriteria;
//import com.anthem.oss.nimbus.core.entity.SearchCriteria.LookupSearchCriteria;
//import com.anthem.oss.nimbus.core.entity.SearchCriteria.QuerySearchCriteria;
//import com.anthem.oss.nimbus.core.util.ClassLoadUtils;
//import com.querydsl.core.types.Predicate;
//
//import groovy.lang.Binding;
//import groovy.lang.GroovyShell;
//import lombok.Getter;
//import lombok.Setter;
//
///**
// * @author Soham Chakravarti
// *
// */
//@Getter @Setter 
//public class DefaultMongoModelRepository implements ModelRepository {
//
//	MongoOperations mongoOps;
//	
//	IdSequenceRepository idSequenceRepo;
//	
//	@Autowired @Qualifier("searchByExample") DBSearch searchByExample;
//	
//	@Autowired @Qualifier("searchByQuery") DBSearch searchByQuery;
//	
//	public DefaultMongoModelRepository(MongoOperations mongoOps, IdSequenceRepository idSequenceRepo) {
//		this.mongoOps = mongoOps;
//		this.idSequenceRepo = idSequenceRepo;
//	}
//	
//	@Override
//	public <T> T _new(Class<T> referredClass, String alias) {
//		T input = ClassLoadUtils.newInstance(referredClass);
//		return _new(referredClass, alias, input);
//	}
//
//	@Override
//	public <T> T _new(Class<T> referredClass, String alias, T input) {
//		PropertyAccessor inputAccessor = PropertyAccessorFactory.forBeanPropertyAccess(input);
//		if(inputAccessor.getPropertyType("id") == Long.class) {
//			inputAccessor.setPropertyValue("id", idSequenceRepo.getNextSequenceId("global"));
//		}
//		mongoOps.insert(input, alias);
//		return input;
//	}
//
//	@Override
//	public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias) {
//		T state = mongoOps.findById(id, referredClass, alias);
//		return state;
//	}
//	
//	private String resolvePath(String path) {
//		String p = StringUtils.replace(path, "/c/", "/");
//		p = StringUtils.replace(p, "/v/", "/");
//		return p;
//	}
//	
//	@Override
//	public <ID extends Serializable, T> T _update(String alias, ID id, String path, T state) {
//		// TODO Soham: Refactor
//		path = resolvePath(path);
//	  
//	  Query query = new Query(Criteria.where("_id").is(id));
//	  Update update = new Update();
//	  if(StringUtils.isBlank(path) || StringUtils.equalsIgnoreCase(path, "/c")) {
//		  mongoOps.save(state, alias);
//	  }
//	  else{
//		  if(StringUtils.equals(path, "/id") || StringUtils.equals(path, "id")) { 
//		  	// if we updated the  document with path "/id", MongoDB is upserting with a new document with same _id but property field as "/id". e.g. if patient document already exist with
//		  	// all the fields populated, it would insert a new patient document with same _id like:
//		  	//	{"_id": NumberLong(1), "/id":NumberLong(1)}
//		  	// whereas there is already a correct patient document as:
//		  	//	{"_id": NumberLong(1), "firstName":"Rakesh"}
//		  	// I think this is because the "id" property gets saved in the monog as "_id" key and so when the next update comes with path="/id", for MongoDB, it would be a new field (non id),
//		  	// hence, ends up creating a new document. for now just returning from this mehtod without going to MongoDB.
//		  		return state;
//		  	}
//		   path = StringUtils.substringAfter(path, "/");
//		   path = path.replaceAll("/", "\\.");
//		   if(state == null)
//			   update.unset(path);
//		   else
//			   update.set(path, state);
//		   mongoOps.upsert(query, update, alias);
//	  } 
//	  return state;
//	 }
//	 
//
//	
//	@Override
//	public <T> T _replace(String alias, T state) {
//		mongoOps.save(state, alias);
//		return state;
//	}
//
//	@Override
//	public void _replace(Param<?> param) {
//
//	}
//
//	@Override
//	public void _replace(List<Param<?>> params) {
//
//	}
//	
//	@Override
//	public <ID extends Serializable, T> T _delete(ID id, Class<T> referredClass, String alias) {
//		Query query = new Query(Criteria.where("_id").is(id));
//		T state = mongoOps.findAndRemove(query, referredClass, alias);
//		return state;
//	}
//	
////	@SuppressWarnings({ "rawtypes", "unchecked" })
////	@Override
////	public <T, C> List<T> _search(Class<T> referredClass, String alias, C criteria) {
////			
////			if(criteria==null) {
////				return mongoOps.findAll(referredClass, alias);
////			}
////			else if(criteria instanceof String) {
////				String filtercriteria = criteria.toString().replaceAll("<", "(").replace(">", ")");
////				Predicate predicate = buildPredicate(filtercriteria, referredClass, alias);
////				SpringDataMongodbQuery query = new SpringDataMongodbQuery<>(mongoOps, referredClass, alias);
////				return query.where(predicate).fetch();	
////			} else {
////				// Build the query with the criteria object
////				Query query = buildQuery(referredClass, alias, criteria);
////				return mongoOps.find(query, referredClass, alias);
////			}
////	}
//	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	//@Override
////	public <T, C> T _search(Class<?> referredClass, String alias, C criteria, Projection projection) {
////		Long count;
////		if(criteria==null) {
////			SpringDataMongodbQuery query = new SpringDataMongodbQuery<>(mongoOps, referredClass, alias);
////			 count = query.fetchCount();
////		}
////		else if(criteria instanceof String) {
////			String filtercriteria = criteria.toString().replaceAll("<", "(").replace(">", ")");
////			Predicate predicate = buildPredicate(filtercriteria, referredClass, alias);
////			SpringDataMongodbQuery query = new SpringDataMongodbQuery<>(mongoOps, referredClass, alias);
////			count = query.where(predicate).fetchCount();
////		} else {
////			// Build the query with the criteria object
////			Query query = buildQuery(referredClass, alias, criteria);
////			count = mongoOps.count(query, referredClass, alias);
////		}
////		return (T) count;
////	}
//	
//	private <C> Query buildQuery(Class<?> referredClass, String alias, C criteria) {
//		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("version");
//		for (Field field : referredClass.getDeclaredFields()) {
//			if (field.getType().isAssignableFrom(String.class)) {
//				field.setAccessible(true);
//				try {
//					String checkString = (String) FieldUtils.readField(field, criteria);
//					if (checkString != null && checkString.isEmpty())
//						matcher = matcher.withIgnorePaths(field.getName());
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				}
//			}
//
//			if (field.isAnnotationPresent(StartsWith.class))
//				matcher = matcher.withMatcher(field.getName(), startsWith());
//		}
//		
//		Example<C> example =  Example.of(criteria, matcher);
//		Criteria c = Criteria.byExample(example);
//		Query query = new Query(c);
//		return query;
//	}
//	
//	private Predicate buildPredicate(String groovyScript,Class<?> referredClass,String alias) {
//		final Binding binding = new Binding();
//		Object obj = createQueryDslClassInstance(referredClass);
//        binding.setProperty(alias, obj);
//        
//        LocalDate localDate = LocalDate.now();
//        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
//        localDateTime.atZone(TimeZone.getDefault().toZoneId());
//       
//        binding.setProperty("todaydate",localDateTime);
//        final GroovyShell shell = new GroovyShell(binding); 
//        return (Predicate)shell.evaluate(groovyScript);
//	}
//	
//	private Object createQueryDslClassInstance(Class<?> referredClass) {
//		Object obj = null;
//		try {
//			String cannonicalQuerydslclass = referredClass.getCanonicalName().replace(referredClass.getSimpleName(), "Q".concat(referredClass.getSimpleName()));
//			Class<?> cl = Class.forName(cannonicalQuerydslclass);
//			Constructor<?> con = cl.getConstructor(String.class);
//			obj = con.newInstance(referredClass.getSimpleName());
//		} catch (Exception e) {
//			
//		}
//		return obj;
//	}
//
//	
//
//	@Override
//	public <T> Object _search(Class<T> referredDomainClass, String alias, LookupSearchCriteria criteria) {
//		return searchByQuery.search(referredDomainClass, alias, criteria);
//	}
//	
//	@Override
//	public <T> Object _search(Class<T> referredDomainClass, String alias, QuerySearchCriteria criteria) {
//		return searchByQuery.search(referredDomainClass, alias, criteria);
//	}
//
//	@Override
//	public <T> Object _search(Class<T> referredDomainClass, String alias, ExampleSearchCriteria<T> criteria) {
//		return searchByExample.search(referredDomainClass, alias, criteria);
//	}
//	
//}
