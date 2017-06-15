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
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelPersistenceHandler;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepository;
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
			
			logit.info(()->"path: "+event.getPath()+ " action: "+event.getType()+" state: "+event.getPayload().getState());
			
			Param<?> param = event.getPayload();
			Model<Object> mRoot = (Model<Object>)param.getRootDomain();
			
			Class<Object> mRootClass = (Class<Object>)mRoot.getConfig().getReferredClass();
			
			String alias = AnnotationUtils.findAnnotation(mRootClass, Repo.class).alias(); // TODO Move this at the repo level, so below method should only pass refId and coreClass
			
			if(StringUtils.isBlank(alias)) {
				alias = AnnotationUtils.findAnnotation(mRootClass, Domain.class).value();
				if(StringUtils.isBlank(alias)) {
					throw new InvalidConfigException("Core Persistent entity must be configured with "+Domain.class.getSimpleName()+" annotation. Not found for root model: "+mRoot);
				} 
			}
				
			Object coreState = mRoot.getState();
			Object coreStateId = mRoot.findParamByPath("/id").getState();
			if(coreStateId == null) {
				rep._new(mRoot.getConfig(), coreState);
				return true;
			}
			
			Serializable coreId = (Serializable)coreStateId; 
			
			String pPath = param.getBeanPath();
			Object pState = param.getState();
			rep._update(alias, coreId, pPath, pState);
			return true;
			
		}
		return false;
		
	}

	
}
