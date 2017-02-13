/**
 * 
 */
package com.anthem.oss.nimbus.core.api.domain.state;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.dsl.binder.ExecutionStateTree;
import com.anthem.nimbus.platform.spec.model.util.JustLogit;
import com.anthem.oss.nimbus.core.InvalidArgumentException;
import com.anthem.oss.nimbus.core.domain.Action;
import com.anthem.oss.nimbus.core.domain.model.state.StateType;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.MappedParam;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ParamStateGateway;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ParamStateRepository;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */

@Component("default.param.state.repository")
@Getter
public class ParamStateRepositoryGateway implements ParamStateGateway {

	private JustLogit logit = new JustLogit(getClass());
	
	@Autowired JavaBeanHandler javaBeanHandler;
	
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
		public <P> Action _set(Param<P> param, P newState) {
			P currState = _get(param);
			if(_equals(newState, currState) == null) return null;
			
			if(isCacheable()) {
				session._set(param, newState);
			}
			//_updateParatmStateTree(param, newState);
			return local._set(param, newState);
		}
		
		public boolean isCacheable() {
			return false;
		}
		
		private <P> void _updateParatmStateTree(Param<P> param, P newState){
			if(param.getType().isNested())
				return;
			ExecutionStateTree executionStateTree = param.getRootModel().getExecutionStateTree();
			if(executionStateTree == null)
				return;
			executionStateTree.addExecutionNode(param, newState);
			
		}		
		
//		public boolean isPersistable() {
//			return true;
//		}
		
		@Override
		public String toString() {
			return "Default Strategy";
		}
	};
	
	@Override
	public <M> M _instantiateAndSet(Param<M> param) {
		return _instantiateAndSet(defaultRepStrategy, param);
	}
	
	public <M> M _instantiateAndSet(ParamStateRepository currRep, Param<M> param) {
		M newState = javaBeanHandler.instantiate(param.getConfig().getReferredClass());
		_setRaw(param, newState);
		return newState;
	}
	
	@Override
	public <P> P _getRaw(Param<P> param) {
		return _getRaw(defaultRepStrategy, param);
	}
	public <P> P _getRaw(ParamStateRepository currRep, Param<P> param) {
		return currRep._get(param);
	}
	
	@Override
	public <P> void _setRaw(Param<P> param, P newState) {
		_setRaw(defaultRepStrategy, param, newState);
	}
	public <P> void _setRaw(ParamStateRepository currRep, Param<P> param, P newState) {
		currRep._set(param, newState);
	}
	
	/***
	 * 1. get value from rep - could be a composite repository hiding all the write-through cache, 1/2nd/3rd level details
	 */
	@Override
	public <P> P _get(Param<P> param) {
		return _get(defaultRepStrategy, param);
	}
	
	public <P> P _get(ParamStateRepository currRep, Param<P> param) {
		logit.trace(()->"[_get] param.path: "+param.getPath()+" with paramRep: "+ currRep);
		
		final P currState;
		if(param.isMapped() && !param.findIfMapped().requiresConversion()) {
			MappedParam<P, ?> mappedFromParam = param.findIfMapped();
			Param<P> mapsToParam = (Param<P>)mappedFromParam.getMapsTo();
			currState = currRep._get(mapsToParam);

		} else {
			currState = currRep._get(param);
		}
		
		return currState;
	}
	
	@Override
	public <P> Action _set(Param<P> param, P newState) {
		return _set(defaultRepStrategy, param, newState);
	}
	
	public <P> Action _set(ParamStateRepository currRep, Param<P> param, P newState) {
		logit.trace(()->"[_set] param.path: "+param.getPath()+" with paramRep: "+ currRep +" newState: "+newState);
		
		// unmapped core/view - nested/leaf: set to param
		if(!param.isMapped()) {
			if(newState==null) {
				return currRep._set(param, null);
			}
			
			if(param.isLeaf()) {
				return currRep._set(param, newState);	
			} else if(param.isCollection()) {
				//reset collection
				_instantiateAndSet(currRep, param);

				if(!(newState instanceof Collection))
					throw new InvalidArgumentException("Collection param with path: "+param.getPath()+" must have argument of type "+Collection.class);
				
				ListParam<P> listParam = param.findIfCollection();
				Collection<P> state = (Collection<P>)newState;
				// add element parameters
				Optional.ofNullable(state)
					.ifPresent(list->
						list.stream()
							.forEach(listParam::add)
					);
				
				return Action._new;
			} else {
				// set nested state as is from passed in domain state
				return currRep._set(param, newState);
			}
		}
		
		// set to current param
		//currRep._set(param, newState);
		
		MappedParam<P, ?> mappedParam = param.findIfMapped();
		Param<P> mapsToParam = (Param<P>)mappedParam.getMapsTo();
		
		// mapped leaf: write to mapped coreParam only if the view param is a leaf param (i.e., not a model) OR if is of same type
		if(!param.findIfMapped().requiresConversion()) {
			Object parentModel = param.getParentModel().instantiateOrGet();//ensure mappedFrom model is instantiated
			return mapsToParam.setState(newState);
			
		} else if(param.isCollection()) {
			if(newState==null) {
				param.getParentModel().instantiateOrGet();//ensure mappedFrom model is instantiated
				return mapsToParam.setState(null);
			}
			
			//reset collection
			_instantiateAndSet(currRep, param);

			if(!(newState instanceof Collection))
				throw new InvalidArgumentException("Collection param with path: "+param.getPath()+" must have argument of type "+Collection.class);
			
			ListParam<P> listParam = param.findIfCollection();
			Collection<P> state = (Collection<P>)newState;
			// add element parameters
			Optional.ofNullable(state)
				.ifPresent(list->
					list.stream()
						.forEach(listParam::add)
				);
			return Action._new;
			
		} 
		
		// mapped nested: ..handling..  <TypeStateAndConfig.Nested<P>>
		StateType.Nested<P> nestedType = param.getType().findIfNested(); 
		Model<P> nestedModel = nestedType.getModel();
		if(nestedModel.templateParams().isNullOrEmpty()) return null;
		
		// iterate child params
		
		for(Param<? extends Object> childParam : nestedModel.getParams()) {
			
			//PropertyDescriptor pd = (childParam.isMapped() ? childParam.findIfMapped().getMapsTo() : childParam).getPropertyDescriptor();
			PropertyDescriptor pd = childParam.getPropertyDescriptor();
			Object childParamState = javaBeanHandler.getValue(pd, newState);
			_set(currRep, (Param<Object>)childParam, childParamState);
		}
		
		//TODO detect change
		return Action._replace;
	}
	
	protected <T> void handleModel(Param<T> pModel, T state) {
		if(pModel.getParentModel().templateParams().isNullOrEmpty()) return;
		
		
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
