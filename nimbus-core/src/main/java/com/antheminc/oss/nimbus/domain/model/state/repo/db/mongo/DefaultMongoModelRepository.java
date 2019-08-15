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
package com.antheminc.oss.nimbus.domain.model.state.repo.db.mongo;

import java.util.Optional;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.RefId;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ValueAccessor;
import com.antheminc.oss.nimbus.domain.model.state.InvalidStateException;
import com.antheminc.oss.nimbus.domain.model.state.repo.IdSequenceRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.MongoIdSequenceRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.MongoDBModelRepositoryOptions;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.MongoDBSearchOperation;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria;
import com.antheminc.oss.nimbus.support.RefIdHolder;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandler;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandlerUtils;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class DefaultMongoModelRepository implements ModelRepository {

	private final MongoOperations mongoOps;
	private final IdSequenceRepository idSequenceRepo;
	private final JavaBeanHandler beanHandler;
	private final MongoDBModelRepositoryOptions options;
	
	public DefaultMongoModelRepository(MongoOperations mongoOps, BeanResolverStrategy beanResolver, 
			MongoDBModelRepositoryOptions options) {
		this.mongoOps = mongoOps;
		this.beanHandler = beanResolver.get(JavaBeanHandler.class);
		this.options = options;
		
		this.idSequenceRepo = new MongoIdSequenceRepository(mongoOps);
	}
	
	@Override
	public <T> RefIdHolder<T> _new(Command cmd, ModelConfig<T> mConfig) {
		T newState = getBeanHandler().instantiate(mConfig.getReferredClass());
		return _new(cmd, mConfig, newState);
	}
	
	@Override
	public <T> RefIdHolder<T> _new(Command cmd, ModelConfig<T> mConfig, T newState) {
		// detect id paramConfig
		ParamConfig<?> pId = Optional.ofNullable(mConfig.getIdParamConfig())
								.orElseThrow(()->new InvalidConfigException("Persistable Entity: "+mConfig.getReferredClass()+" must be configured with @Id param."));
		
		Long id = getIdSequenceRepo().getNextSequenceId(mConfig.getRepoAlias());
		
		ValueAccessor va = JavaBeanHandlerUtils.constructValueAccessor(mConfig.getReferredClass(), pId.getCode());
		getBeanHandler().setValue(va, newState, id);
		
		RefId<?> refId = RefId.with(id);
		return new RefIdHolder<>(refId, newState);
	}

	@Override
	public <T> T _save(String alias, T state) {
		getMongoOps().save(state, alias);
		return state;
	}
	
	@Override
	public void _save(Param<?> param) {
		@SuppressWarnings("unchecked")
		Model<Object> mRoot = (Model<Object>)param.getRootDomain();
		
		String idParamCode = mRoot.getConfig().getIdParamConfig().getCode();
		Object coreStateId = mRoot.findParamByPath(idParamCode).getState();
		if(coreStateId == null) {
			Command rootCmd = mRoot.getRootExecution().getRootCommand();
			_new(rootCmd, mRoot.getConfig(), mRoot.getState());
			return;
		}
		
		Object pState = param.getState();
		_update(param, pState);
	}
	
	@Override
	public <T> T _get(Command cmd, ModelConfig<T> mConfig) {
		Long id = RefId.nullSafeGetId(cmd.getRefId(Type.DomainAlias));
		if(id==null)
			return null;
		
		T state = getMongoOps().findById(id, mConfig.getReferredClass(), mConfig.getRepoAlias());
		return state;
	}
	
	private String resolvePath(String path) {
		String p = StringUtils.replace(path, "/c/", "/");
		p = StringUtils.replace(p, "/v/", "/");
		return p;
	}
	
	@Override
	public <T> T _update(Param<?> param, T state) {
		// TODO Soham: Refactor
		String path = resolvePath(param.getBeanPath());
	  
		Query query = new Query(Criteria.where("_id").is(param.getRootExecution().getRootCommand().getRefId(Type.DomainAlias)));
		Update update = new Update();
		if(StringUtils.isBlank(path) || StringUtils.equalsIgnoreCase(path, "/c")) {
			getMongoOps().save(state, param.getRootDomain().getConfig().getRepoAlias());
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
			
			String repoAlias = param.getRootDomain().getConfig().getRepoAlias();
			if (StringUtils.isBlank(repoAlias)) {
				throw new InvalidConfigException("Core Persistent entity must be configured with "
						+ Domain.class.getSimpleName() + " annotation. Not found for root model: " + param.getRootDomain());
			}
			getMongoOps().upsert(query, update, repoAlias);
		} 
		
		return state;
	}
	
	
	@Override
	public <T> T _delete(Param<?> param) {
		RefId<?> refId = param.getRootExecution().getRootCommand().getRefId(Type.DomainAlias);
		Class<T> referredClass = (Class<T>)param.getRootDomain().getConfig().getReferredClass();
		String alias = param.getRootDomain().getConfig().getRepoAlias();
		
		Long id = RefId.nullSafeGetId(refId);
		if(id==null)
			throw new InvalidStateException("Id must not be null for delete in param: "+param);
		
		Query query = new Query(Criteria.where("_id").is(id));
		T state = getMongoOps().findAndRemove(query, referredClass, alias);
		return state;
	}

	@Override
	public <T> Object _search(Param<?> param, Supplier<SearchCriteria<?>> criteria) {
		SearchCriteria<?> sc = criteria.get();
		Class<?> referredClass = param.getRootDomain().getConfig().getReferredClass();
		String alias = param.getRootDomain().getConfig().getRepoAlias();
		Optional<MongoDBSearchOperation> searchOperation = getOptions().getSearchOperations().stream().filter(o -> o.shouldAllow(sc)).findFirst();
		if (!searchOperation.isPresent()) {
			throw new FrameworkRuntimeException("Unable to determine search operation for search criteria: " + sc);
		}
		return searchOperation.get().search(referredClass, alias, sc);
	}
}
