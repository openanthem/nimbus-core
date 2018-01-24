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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelPersistenceHandler;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * @author Rakesh Patel
 *
 */
public class DefaultMongoModelPersistenceHandler implements ModelPersistenceHandler {

	JustLogit logit = new JustLogit(getClass());
	
	ModelRepository rep;
	
	public DefaultMongoModelPersistenceHandler(ModelRepository rep) {
		this.rep = rep;
	}

	@Override
	public boolean handle(List<ModelEvent<Param<?>>> modelEvents) {
		
		if(CollectionUtils.isEmpty(modelEvents)) 
			return false;
		
		for(ModelEvent<Param<?>> event: modelEvents) {
			
			logit.trace(()->"path: "+event.getPath()+ " action: "+event.getType()+" state: "+event.getPayload().getState());
			
			Param<?> param = event.getPayload();
			
			Model<Object> model = findIfNestedAndHasDomain(param);
			Class<Object> modelClass = (Class<Object>)model.getConfig().getReferredClass();
			
			Repo repo = AnnotationUtils.findAnnotation(modelClass, Repo.class);
			
			String alias = repo != null && StringUtils.isNotBlank(repo.alias()) ? repo.alias() : model.getConfig().getAlias();
			
			
			Object coreState = model.getState();
			Object coreStateId = model.findParamByPath("/id").getState();
			final Serializable coreId;
			if(coreStateId == null) {
				coreState = rep._new(model.getConfig(), coreState);
				rep._save(alias, coreState);
				return true;
			}
			
			coreId = (Serializable)coreStateId; 
			
			String pPath = param.getBeanPath();
			Object pState = param.getState();
			rep._update(alias, coreId, pPath, pState);
			return true;
			
		}
		return false;
		
	}

	
	private Model<Object> findIfNestedAndHasDomain(Param<?> param) {
		if(param.isNested() ) {
			Model<Object> model = (Model<Object>)param.findIfNested();
			Class<Object> modelClass = (Class<Object>)model.getConfig().getReferredClass();
			
			Repo repo = AnnotationUtils.findAnnotation(modelClass, Repo.class);
			
			if(Repo.Database.isPersistable(repo)) {
				Domain domain = AnnotationUtils.findAnnotation(modelClass, Domain.class);
				
				if(repo != null && domain != null) {
					return model;
				}
			}
		}
		return (Model<Object>)param.getRootDomain();
	}
	
}
