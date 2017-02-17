/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.util.JustLogit;

/**
 * @author Rakesh Patel
 *
 */
@Component("rep_mongodb_handler")
public class DefaultMongoModelPersistenceHandler implements ModelPersistenceHandler {

	JustLogit logit = new JustLogit(getClass());
	
	@Autowired
	@Qualifier("rep_mongodb")
	ModelRepository rep;
	
	@Override
	public boolean handle(List<ModelEvent<Param<?>>> modelEvents) {
		
		if(CollectionUtils.isEmpty(modelEvents)) 
			return false;
		
		for(ModelEvent<Param<?>> event: modelEvents) {
			
			logit.info(()->"path: "+event.getPath()+ " action: "+event.getType()+" state: "+event.getPayload());
			
			Param<?> param = event.getPayload();
			
			Model<?> mRoot = null;
			
			if(param instanceof Param<?>) {
				Param<?> p = (Param<?>) param;
				mRoot = p.getRootModel();
			}
			else{
				Model<?> p = (Model<?>) param;
				mRoot = p.getRootModel();
			}
			//StateAndConfig<?, ?> mRoot = param.getRootParent();
			Class<Object> mRootClass = (Class<Object>)mRoot.getConfig().getReferredClass();
			
			Domain coreRoot = AnnotationUtils.findAnnotation(mRootClass, Domain.class);
			if(coreRoot==null) {
				throw new InvalidConfigException("Core Persistent entity must be configured with "+Domain.class.getSimpleName()+" annotation. Not found for root model: "+mRoot);
			} 
				
			String coreRootAlias = coreRoot.value();
			
			Object coreState = mRoot.getState();
			Object coreStateId = mRoot.findParamByPath("/id").getState();
			if(coreStateId==null) {
				//repoFactory.getPersistenceHandler(cmd).getRepo()._new(mRootClass, coreRootAlias, coreState);
				rep._new(mRootClass, coreRootAlias, coreState);
				return true;
			}
			
			String coreId = coreStateId.toString(); 
			
			if(param instanceof Param<?>) {
				Param<?> p = (Param<?>) param;
				String pPath = p.getPath();
				Object pState = p.getState();
				
				rep._update(coreRootAlias, coreId, pPath, pState);
				return true;
			}
			else {
				Model<?> p = (Model<?>) param;
				String pPath = p.getPath();
				Object pState = p.getState();
				
				rep._update(coreRootAlias, coreId, pPath, pState);
				return true;
			}
			
			
		}
		return false;
		
	}

	
}
