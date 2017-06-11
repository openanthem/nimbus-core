/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db.mongo;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.repo.IdSequenceRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.DBSearch;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepository;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.ExampleSearchCriteria;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.LookupSearchCriteria;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.QuerySearchCriteria;
import com.anthem.oss.nimbus.core.utils.JavaBeanHandler;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultMongoModelRepository implements ModelRepository {

	private final MongoOperations mongoOps;
	private final IdSequenceRepository idSequenceRepo;
	private final JavaBeanHandler beanHandler;
	
	@Autowired @Qualifier("searchByExample") DBSearch searchByExample;
	
	@Autowired @Qualifier("searchByQuery") DBSearch searchByQuery;

	public DefaultMongoModelRepository(MongoOperations mongoOps, IdSequenceRepository idSequenceRepo, BeanResolverStrategy beanResolver) {
		this.mongoOps = mongoOps;
		this.idSequenceRepo = idSequenceRepo;
		this.beanHandler = beanResolver.get(JavaBeanHandler.class);
	}
	
	@Override
	public <T> T _new(ModelConfig<T> mConfig) {
		T newState = beanHandler.instantiate(mConfig.getReferredClass());
		return _new(mConfig, newState);
	}
	
	@Override
	public <T> T _new(ModelConfig<T> mConfig, T newState) {
		// detect id paramConfig
		ParamConfig<?> pId = Optional.ofNullable(mConfig.getIdParam())
								.orElseThrow(()->new InvalidConfigException("Persistable Entity: "+mConfig.getReferredClass()+" must be configured with @Id param."));
		
		String id = String.valueOf(idSequenceRepo.getNextSequenceId(mConfig.getDomainAlias()));
		
		PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(mConfig.getReferredClass(), pId.getCode());
		beanHandler.setValue(pd, newState, id);
		
		String alias = mConfig.getRepo() != null && StringUtils.isNotBlank(mConfig.getRepo().alias()) ? mConfig.getRepo().alias():mConfig.getDomainAlias();
		mongoOps.save(newState, alias);
		
		return newState;
	}

	@Override
	public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias) {
		T state = mongoOps.findById(id, referredClass, alias);
		return state;
	}
	
	private String resolvePath(String path) {
		String p = StringUtils.replace(path, "/c/", "/");
		p = StringUtils.replace(p, "/v/", "/");
		return p;
	}
	
	@Override
	public <ID extends Serializable, T> T _update(String alias, ID id, String path, T state) {
		// TODO Soham: Refactor
		path = resolvePath(path);
	  
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
	public <T> Object _search(Class<T> referredDomainClass, String alias, LookupSearchCriteria criteria) {
		return searchByQuery.search(referredDomainClass, alias, criteria);
	}
	
	@Override
	public <T> Object _search(Class<T> referredDomainClass, String alias, QuerySearchCriteria criteria) {
		return searchByQuery.search(referredDomainClass, alias, criteria);
	}

	@Override
	public <T> Object _search(Class<T> referredDomainClass, String alias, ExampleSearchCriteria<T> criteria) {
		return searchByExample.search(referredDomainClass, alias, criteria);
	}
	
}
