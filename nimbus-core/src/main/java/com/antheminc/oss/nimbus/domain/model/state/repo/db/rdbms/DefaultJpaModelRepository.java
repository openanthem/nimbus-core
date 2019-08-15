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
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.IdClass;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.RefId;
import com.antheminc.oss.nimbus.domain.cmd.RefId.SyntheticKeyValueId;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ValueAccessor;
import com.antheminc.oss.nimbus.domain.model.state.internal.ExecutionEntity;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.DBSearchOperation;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria;
import com.antheminc.oss.nimbus.support.EnableAPIMetricCollection;
import com.antheminc.oss.nimbus.support.RefIdHolder;
import com.antheminc.oss.nimbus.support.pojo.ClassLoadUtils;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandler;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandlerUtils;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@EnableAPIMetricCollection
@Getter
public class DefaultJpaModelRepository implements ModelRepository {

	//private final EntityManagerFactory entityManagerFactory;
	private final JavaBeanHandler beanHandler;
	private final DBSearchOperation dbSearch;
	
	@Autowired
	@PersistenceContext
	private EntityManager em;
	
	public DefaultJpaModelRepository(EntityManagerFactory entityManagerFactory, JavaBeanHandler beanHandler, DBSearchOperation dbSearch) {
		//this.entityManagerFactory = entityManagerFactory;
		this.beanHandler = beanHandler;
		this.dbSearch = dbSearch;
	}
	
	private EntityManager getOrCreateEntityManager() {
		return em;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> SimpleJpaRepository<T, Object> createJPARepo(EntityManager entityManager, ModelConfig<T> mConfig) {
		JpaEntityInformation<T, Object> info = (JpaEntityInformation<T, Object>)JpaEntityInformationSupport.getEntityInformation(mConfig.getReferredClass(), entityManager);
		
		return new SimpleJpaRepository<>(info, entityManager);
	}	

	@Transactional
	@Override
	public <T> RefIdHolder<T> _new(Command cmd, ModelConfig<T> mConfig) {
		T newState = getBeanHandler().instantiate(mConfig.getReferredClass());
		return _new(cmd, mConfig, newState);
	}

	@Transactional
	@SuppressWarnings("rawtypes")
	@Override
	public <T> RefIdHolder<T> _new(Command cmd, ModelConfig<T> mConfig, T newState) {
		
		RefId<?> refId = cmd.getRefId(Type.DomainAlias);

		if(refId==null) {
			EntityManager entityManager = getOrCreateEntityManager();
			
			refId = Optional.ofNullable(entityManager.getMetamodel().entity(mConfig.getReferredClass()).getIdType())
				.map(idType-> {
					SessionImplementor hSession = entityManager.unwrap(SessionImplementor.class);
					EntityPersister persister = hSession.getEntityPersister(null, newState);
					Serializable generatedId = persister.getIdentifierGenerator().generate(hSession, newState);
					return (RefId)RefId.with(String.valueOf(generatedId));
				})
				.orElseGet(()->{
					// generate synthetic refId: scenarios where Id is composite (@IdClass or multiple columns involved in making a PK)
					return RefId.with();
				});
			
		} else {
			if(refId.isKeyValue())
				setRefMapEntries(refId.findIfKeyValue(), mConfig, newState);
			
		}
		
		setRefId(mConfig, newState, refId.getId());
		return new RefIdHolder<>(refId, newState);
	}
	
	// common
	protected <T> void setRefId(ModelConfig<T> mConfig, T entity, Long refId) {
		ParamConfig<?> pId = Optional.ofNullable(mConfig.getIdParamConfig())
				.orElseThrow(()->new InvalidConfigException("Persistable Entity: "+mConfig.getReferredClass()+" must be configured with @Id param."));
		
		ValueAccessor va = JavaBeanHandlerUtils.constructValueAccessor(mConfig.getReferredClass(), pId.getCode());
		getBeanHandler().setValue(va, entity, refId);
	}

	// common	
	protected void setRefMapEntries(RefId<Map<String, String>> refIdMap, ModelConfig<?> mConfig, Object entity) {
		refIdMap.getValue().entrySet().stream()
			.forEach(e->{
				String key = e.getKey();
				
				String path = StringUtils.replace(key, ".", "/");
				ParamConfig<?> p = Optional.ofNullable(mConfig.findParamByPath(path))
						.orElseThrow(()->new InvalidConfigException("RefId KeyValue param code: "+key+" not found in model: "+mConfig.getReferredClass()));
				
				String value = e.getValue();
				Object typeAdjustedValue = ClassLoadUtils.toObject(p.getReferredClass(), value);
				
				ValueAccessor va = JavaBeanHandlerUtils.constructValueAccessor(entity.getClass(), p.getCode());
				getBeanHandler().setValue(va, entity, typeAdjustedValue);
			});
	}

	@Transactional
	@Override
	public <T> T _get(Command cmd, ModelConfig<T> mConfig) {
		@SuppressWarnings("unchecked")
		RefId<Object> refId = (RefId<Object>)Optional.ofNullable(cmd.getRefId(Type.DomainAlias))
				.orElseThrow(()->new InvalidConfigException("_get call must have RefId in cmd: "+cmd+" for model config: "+mConfig));
		
		
		RefId<Map<String, String>> refIdMap = Optional.ofNullable(refId.findIfComposite())
				.map(RefId<SyntheticKeyValueId>::getValue)
				.map(SyntheticKeyValueId::getKeyValue)
				.orElse(
						Optional.ofNullable(refId.findIfKeyValue())
						.orElse(null));
		
		
		// use key value
		if(refIdMap!=null) 
			return getByKeyValue(refIdMap, mConfig);
		
		// use id as long type - any other type would be accounted as key value
		SimpleJpaRepository<T, Object> jpaRepo = createJPARepo(getOrCreateEntityManager(), mConfig);
		return jpaRepo.findById(refId.getId()).orElse(null);
	}
	
	protected <T> T getByKeyValue(RefId<Map<String, String>> refIdMap, ModelConfig<T> domainConfig) {
		EntityManager entityManager = getOrCreateEntityManager();
			
		try {
			Object pkEntity = buildIdentityInstance(entityManager, refIdMap, domainConfig);
			T entity = entityManager.find(domainConfig.getReferredClass(), pkEntity);
			if (entity == null)
				return null;
			// set generated refId into entity
			Long refId = refIdMap.getId();
			setRefId(domainConfig, entity, refId);
			return entity;
		} finally {
			entityManager.close();
		}
	}
	
	protected Object buildIdentityInstance(EntityManager entityManager, RefId<Map<String, String>> refIdMap, ModelConfig<?> mConfig) {
		@SuppressWarnings("unchecked")
		Class<?> pkClass = Optional.ofNullable(entityManager.getMetamodel().entity(mConfig.getReferredClass()).getIdType())
				.map(type->type.getJavaType())
				.orElseGet(()->{
					
					IdClass idClass = AnnotationUtils.findAnnotation(mConfig.getReferredClass(), IdClass.class);
					return Optional.ofNullable(idClass)
							.map(IdClass::value)
							.orElseThrow(()->new InvalidConfigException("JPA Entity must be configured with @IdClass or @EmbeddedId, but found none for: "+mConfig.getReferredClass()));
				});
		
		Object entity = getBeanHandler().instantiate(pkClass);
		setRefMapEntries(refIdMap, mConfig, entity);
		
		return entity;
	}

	@Transactional
	@Override
	public <T> T _update(Param<?> param, T state) {
		_save(param);
		return state;
	}
	
	@Transactional
	@Override
	public <T> T _save(String alias, T state) {
		EntityManager entityManager = getOrCreateEntityManager();

		entityManager.merge(state);
		entityManager.flush();
		
		return state;
	}
	
	@Transactional
	@Override
	public void _save(Param<?> param) {
		Model root = param.getRootDomain();
		
		boolean isRootNew = param.getRootExecution().isNew();
		Object state = root.getState();

		EntityManager entityManager = getOrCreateEntityManager();
		Object mergedState = entityManager.merge(state);
		entityManager.flush();

		if(isRootNew) {
			param.getRootExecution().setNew(false);
			
			Object id = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(state);
			
			SimpleJpaRepository<Object, Object> jpaRepo = createJPARepo(entityManager, root.getConfig());
			Object attachedState = jpaRepo.findById(id).orElse(null);
			
			ExecutionModel execModel = param.getRootExecution();
			ExecutionEntity execEntity = (ExecutionEntity)execModel.getState();
			execEntity.setCore(attachedState);
		}
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public <T> T _delete(Param<?> param) {
		Model<?> root = param.getRootDomain();
		
		T state = (T)param.getState();
		if(root.isNew())
			return state;
		
		EntityManager entityManager = getOrCreateEntityManager();
		
		Object mergedState = entityManager.contains(state) ? state : entityManager.merge(state);

		entityManager.remove(mergedState);
		entityManager.flush();
		
		return state;
	} 
	
	@Override
	public <T> Object _search(Param<?> param, Supplier<SearchCriteria<?>> criteria) {
		SearchCriteria<?> sc = criteria.get();
		Class<?> referredClass = param.getRootDomain().getConfig().getReferredClass();
		String alias = param.getRootDomain().getConfig().getRepoAlias();
		
		return dbSearch.search(referredClass, alias, sc);
	}

}
