/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.contract.repository.ParamStateRepository;
import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ExecutionStateTree;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */

@Component("default.param.state.repository")
@Getter
public class ParamStateRepositoryGateway implements ParamStateRepository {
	
	@Autowired	@Qualifier("default.param.state.rep_local")
	private ParamStateRepository local;
	
	//@Autowired	@Qualifier("default.param.state.rep_session")
	private ParamStateRepository session;

	/*
	@Autowired	@Qualifier("default.param.state.rep_db")
	private ParamStateRepository db;
	*/
	private ParamStateRepository defaultRepStrategy = new ParamStateRepository() {
		
		/**
		 * Local is always kept, but follows behind cache if configured.
		 * 
		 * 1. If cache=true, then retrieve state from cache AND set to local before returning if local state is different
		 * 2. If cache=false, then 
		 */
		@Override
		public <P> P _get(Param<P> param) {
			if(isCacheable()) {
				P cachedState = session._get(param);
				P localState  = local._get(param);
				if(_equals(cachedState, localState) != null) {
					local._set(param, cachedState);
					localState = cachedState;
				}
				return localState;
			} else {
				P localState  = local._get(param);
				return localState;
			}
		}
		
		@Override
		public <P> void _set(Param<P> param, P newState) {
			P currState = _get(param);
			if(_equals(newState, currState) == null) return;
			
			if(isCacheable()) {
				session._set(param, newState);
			}
			_updateParatmStateTree(param, newState);
			local._set(param, newState);
		}
		
		public boolean isCacheable() {
			return false;
		}
		
		private <P> void _updateParatmStateTree(Param<P> param, P newState){
			if(param.getType().isNested())
				return;
			ExecutionStateTree executionStateTree = param.getRootParent().getExecutionStateTree();
			if(executionStateTree == null)
				return;
			executionStateTree.addExecutionNode(param, newState);
			
		}		
		
//		public boolean isPersistable() {
//			return true;
//		}
	};
	
	/***
	 * 1. get value from rep - could be a composite repository hiding all the write-through cache, 1/2nd/3rd level details
	 */
	@Override
	public <P> P _get(Param<P> param) {
		return _get(defaultRepStrategy, param);
	}
	
	public <P> P _get(ParamStateRepository currRep, Param<P> param) {
		final P currState;
		if(param.isMapped()) {
			Param<P> mapsToParam = param.getMapsTo();
			currState = mapsToParam.getState();
		} else {
			currState = currRep._get(param);
		}
		
		return currState;
	}
	
	@Override
	public <P> void _set(Param<P> param, P newState) {
		_set(defaultRepStrategy, param, newState);
	}
	
	public <P> void _set(ParamStateRepository currRep, Param<P> param, P newState) {
		currRep._set(param, newState);
		
		// write to mapped coreParam only if the view param is a leaf param (i.e., not a model) 
		if(param.isMapped() && param.getConfig().isLeaf()) {
			Param<P> mapsToParam = param.getMapsTo();
			mapsToParam.setState(newState);
		}
	}
	
	public static <P> Action _equals(P newState, P currState) {
		//00
		if(newState==null && currState==null) return null;
		
		//01
		if(newState==null && currState!=null) return Action._delete;
		
		//10
		if(newState!=null && currState==null) return Action._new;
		
		//11
		boolean isEqual = currState.equals(newState);
		return isEqual ? null : Action._update;
	}
}
