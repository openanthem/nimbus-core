/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db.mongo;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ModelPersistenceHandler;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ModelRepository;
import com.anthem.oss.nimbus.core.util.JustLogit;

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
