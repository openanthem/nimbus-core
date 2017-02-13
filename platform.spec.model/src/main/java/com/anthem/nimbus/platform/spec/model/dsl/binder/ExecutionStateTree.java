/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */


public class ExecutionStateTree implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String TRIGGER_PARAM_KEY = "triggerParams";
	public static final String ENABLE_ADD_NODE_TO_STATE_KEY = "enabelAddNodeToState";
	private Map<String,List<ExecutionStateTreeNode>> executionStateTreeNodes = new HashMap<String,List<ExecutionStateTreeNode>>();
	
	/**
	 * 
	 * @param triggerParam
	 * @param triggeredParam
	 * @param oldState
	 * @param newState
	 */
	public void addExecutionNode(Param<?> triggeredParam, Object newState){
		Boolean key = getEnableAddToStateTreeKey();
		Param<?> triggerParam = getTriggerParam();
		if(triggerParam == null || !key)
			return;
		String triggerParamPath = triggerParam.getPath();
		String triggeredParamPath = triggeredParam.getPath();
		if(triggerParamPath.equals(triggeredParamPath)){
			return;
		}
		List<ExecutionStateTreeNode> executionNodes = executionStateTreeNodes.get(triggerParamPath);
		if(executionNodes == null){
			executionNodes = new ArrayList<ExecutionStateTreeNode>();
			executionStateTreeNodes.put(triggerParamPath, executionNodes);
		}
		ExecutionStateTreeNode node = new ExecutionStateTreeNode();
		node.setParamPath(triggeredParamPath);
		node.setOldState(triggeredParam.getState());
		node.setNewState(newState);
		executionNodes.add(node);
	}
	
	/**
	 * 
	 * @param triggerParam
	 */
	public void resetStateForParameter(){
		setEnableAddToStateTreeKey(Boolean.FALSE);
		Param<?> triggerParam = getTriggerParam();
		String triggerParamPath = triggerParam.getPath();
		List<ExecutionStateTreeNode> executionNodes = executionStateTreeNodes.get(triggerParamPath);
		if(executionNodes != null){
			Model<?> parentModel = triggerParam.getRootModel();
			for(ExecutionStateTreeNode en: executionNodes){
				Param<Object> param = parentModel.findParamByPath(en.getParamPath());
				Object currentState = param.getState();
				Object stateInStateTree = en.getNewState();
				if(currentState.equals(stateInStateTree)){
					param.setState(en.getOldState());
				}
			}
			executionStateTreeNodes.put(triggerParamPath,null);
		}
		setEnableAddToStateTreeKey(Boolean.TRUE);
	}
	
	/**
	 * 
	 * @return
	 */
	private Param<?> getTriggerParam(){
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return (Param<?>)requestAttributes.getAttribute(TRIGGER_PARAM_KEY, RequestAttributes.SCOPE_REQUEST);
	}
	
	/**
	 * 
	 * @param key
	 */
	private void setEnableAddToStateTreeKey(Boolean key){
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		requestAttributes.setAttribute(ENABLE_ADD_NODE_TO_STATE_KEY, key, RequestAttributes.SCOPE_REQUEST);
	}
	
	/**'
	 * 
	 * @return
	 */
	private Boolean getEnableAddToStateTreeKey(){
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Boolean key = (Boolean)requestAttributes.getAttribute(ENABLE_ADD_NODE_TO_STATE_KEY,RequestAttributes.SCOPE_REQUEST);
		if(key == null){
			key = Boolean.FALSE;
		}
		return key;
	}
}
