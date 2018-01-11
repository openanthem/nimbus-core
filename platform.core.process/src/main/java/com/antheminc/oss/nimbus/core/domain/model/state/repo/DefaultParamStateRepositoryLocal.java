/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.repo;

import java.beans.PropertyDescriptor;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.core.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.core.InvalidOperationAttemptedException;
import com.antheminc.oss.nimbus.core.domain.command.Action;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.ListElemParam;
import com.antheminc.oss.nimbus.core.util.JustLogit;
import com.antheminc.oss.nimbus.core.utils.JavaBeanHandler;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultParamStateRepositoryLocal implements ParamStateRepository {

	@Autowired JavaBeanHandler javaBeanHandler;
	
	private JustLogit logit = new JustLogit(getClass());
	
	public DefaultParamStateRepositoryLocal(JavaBeanHandler javaBeanHandler) {
		this.javaBeanHandler = javaBeanHandler;
	}
	
	@Override
	public <P> P _get(EntityState.Param<P> param) {
		if(param.isCollectionElem()) {
			ListElemParam<P> pElem = param.findIfCollectionElem();
			
			// instantiate collection if needed
			List<P> coreList = pElem.getParentModel().instantiateOrGet();
			
			int index = pElem.getParentModel().templateParams().indexOf(pElem);
			return coreList.size()>index && index!=-1 ? coreList.get(index) : null;
			
		} else {
			PropertyDescriptor pd = param.getPropertyDescriptor();
			Object target = param.getParentModel().getState();
			return javaBeanHandler.getValue(pd, target);
		}
	}
	
	@Override
	public <P> Action _set(EntityState.Param<P> param, P newState) {
		logit.trace(()->"_set@enter -> param.path: "+param.getPath()+" newState: "+newState);
		
		if(param.isCollectionElem()) {
			EntityState.ListElemParam<P> pElem = param.findIfCollectionElem();
			
			// instantiate collection if needed
			List<P> entityStateList = pElem.getParentModel().instantiateOrGet();

			// boundary condition check: entity-list-size cannot be more than model-list-size
			int modelListSize = pElem.getParentModel().templateParams().size();
			
			if(entityStateList.size() > modelListSize) {
				throw new InvalidOperationAttemptedException(
						"Attemted to set in collection where entity state size :"+entityStateList.size()
						+" is more than model list size of:"+modelListSize
						+" for param.path: "+param.getPath());
				
			} /*else if(modelListSize > entityStateList.size()+1) {
				throw new InvalidOperationAttemptedException(
						"Attemted to set in collection where model list size :"+modelListSize
						+" is more than entity list size of:"+entityStateList.size()+" by difference greater than 1"
						+" for param.path: "+param.getPath());
			}*/
			
			
			logit.trace(()->"_set@colElem -> modelListSize: "+modelListSize+" entityStateList.size: "+entityStateList.size()+" with elemId: "+pElem.getElemId());
			
//			if(modelListSize == entityStateList.size()+1) { //add immediate next
//				entityStateList.add(newState);
//				return Action._new;
//				
//			} 
			
			int modelListElemIndex = pElem.getParentModel().templateParams().indexOf(pElem);

			if(modelListElemIndex==-1 || modelListElemIndex == entityStateList.size()) {
				entityStateList.add(newState);
				return Action._new;
				
			} else if(modelListElemIndex < entityStateList.size()) {
				entityStateList.set(modelListElemIndex, newState);
				return Action._replace;
				
			} else {
				throw new InvalidOperationAttemptedException("Param :+"+param+"\n\tCannot add/set to collection entity state when modelListElemIndex:"+modelListElemIndex
						+" is greater than entityStateList.size(): "+entityStateList.size());
			}
			
			
			
			
		} else {
			PropertyDescriptor pd = param.getPropertyDescriptor();
			
			//ensure that the target parent model is instantiated
			Object target = param.getParentModel().instantiateOrGet();
			if(target==null) throw new FrameworkRuntimeException("Target must not be null for setting in property: "+pd+" with value: "+ newState);
			
			javaBeanHandler.setValue(pd, target, newState);
			
			//TODO change detection
			return Action._replace;
		}
	}

}
