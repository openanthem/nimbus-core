/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.repository;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;
import com.anthem.nimbus.platform.spec.model.dsl.ModelEvent;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Model;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param;
import com.anthem.nimbus.platform.spec.model.dsl.config.Config;
import com.anthem.nimbus.platform.spec.model.exception.InvalidConfigException;
import com.anthem.nimbus.platform.spec.model.util.JustLogit;

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
	public boolean handle(List<ModelEvent<StateAndConfig<?,?>>> modelEvents) {
		
		if(CollectionUtils.isEmpty(modelEvents)) 
			return false;
		
		for(ModelEvent<StateAndConfig<?,?>> event: modelEvents) {
			
			logit.info(()->"path: "+event.getPath()+ " action: "+event.getType()+" state: "+event.getPayload().getState());
			
			StateAndConfig<?,?> param = event.getPayload();
			
			StateAndConfig.Model<?, ?> mRoot = null;
			
			if(param instanceof Param<?>) {
				Param<?> p = (Param<?>) param;
				mRoot = p.getRootParent();
			}
			else{
				Model<?,?> p = (Model<?,?>) param;
				mRoot = p.getRootParent();
			}
			//StateAndConfig<?, ?> mRoot = param.getRootParent();
			Class<Object> mRootClass = (Class<Object>)mRoot.getConfig().getReferredClass();
			
			CoreDomain coreRoot = AnnotationUtils.findAnnotation(mRootClass, CoreDomain.class);
			if(coreRoot==null) {
				throw new InvalidConfigException("Core Persistent entity must be configured with "+CoreDomain.class.getSimpleName()+" annotation. Not found for root model: "+mRoot);
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
				Model<?,?> p = (Model<?,?>) param;
				String pPath = p.getPath();
				Object pState = p.getState();
				
				rep._update(coreRootAlias, coreId, pPath, pState);
				return true;
			}
			
			
		}
		return false;
		
	}

	
}
