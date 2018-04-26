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
package com.antheminc.oss.nimbus.domain.model.state.repo.db.mongo;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ValueAccessor;
import com.antheminc.oss.nimbus.domain.model.state.repo.IdSequenceRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.DBSearch;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria.ExampleSearchCriteria;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandler;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandlerUtils;

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
		ParamConfig<?> pId = Optional.ofNullable(mConfig.getIdParamConfig())
								.orElseThrow(()->new InvalidConfigException("Persistable Entity: "+mConfig.getReferredClass()+" must be configured with @Id param."));
		
		Repo repo = mConfig.getRepo();
		
		Long id = idSequenceRepo.getNextSequenceId(repo != null && StringUtils.isNotBlank(repo.alias())?repo.alias():mConfig.getAlias());
		
		ValueAccessor va = JavaBeanHandlerUtils.constructValueAccessor(mConfig.getReferredClass(), pId.getCode());
		beanHandler.setValue(va, newState, id);
		
		String alias = mConfig.getRepo() != null && StringUtils.isNotBlank(mConfig.getRepo().alias()) ? mConfig.getRepo().alias():mConfig.getAlias();
		//mongoOps.save(newState, alias);
		
		return newState;
	}

	@Override
	public <ID extends Serializable, T> T _save(String alias, T state) {
		mongoOps.save(state, alias);
		return state;
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
	public <T> Object _search(Class<T> referredDomainClass, String alias, Supplier<SearchCriteria<?>> criteria) {
		SearchCriteria<?> sc = criteria.get();
		if(sc instanceof ExampleSearchCriteria) {
			return searchByExample.search(referredDomainClass, alias, sc);
		}
		return searchByQuery.search(referredDomainClass, alias, sc);
	}

}
