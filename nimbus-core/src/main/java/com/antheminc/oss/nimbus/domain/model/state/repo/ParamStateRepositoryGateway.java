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
package com.antheminc.oss.nimbus.domain.model.state.repo;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;

import com.antheminc.oss.nimbus.InvalidArgumentException;
import com.antheminc.oss.nimbus.UnsupportedScenarioException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.defn.Converters.ParamConverter;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig.MappedParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.MappedParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ValueAccessor;
import com.antheminc.oss.nimbus.domain.model.state.StateType;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.antheminc.oss.nimbus.support.pojo.JavaBeanHandler;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */

@Getter
public class ParamStateRepositoryGateway implements ParamStateGateway {

	private JustLogit logit = new JustLogit(ParamStateRepositoryGateway.class);
	
	JavaBeanHandler javaBeanHandler;
	
	private ParamStateRepository local;
	
	//@Autowired	@Qualifier("default.param.state.rep_session")
	private ParamStateRepository session;
	
	private ParamStateRepository detachedStateRepository;

	public ParamStateRepositoryGateway(JavaBeanHandler javaBeanHandler, ParamStateRepository local, BeanResolverStrategy beanResolver) {
		this.javaBeanHandler = javaBeanHandler;
		this.local = local;
		this.detachedStateRepository = beanResolver.get(ParamStateRepository.class, "param.state.rep_detached");
		
	}
	
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
		
//		public boolean isPersistable() {
//			return true;
//		}
		
		@Override
		public String toString() {
			return "Default Strategy";
		}
	};
	
	@Override
	public <T> T instantiate(Class<T> clazz) {
		return javaBeanHandler.instantiate(clazz);
	}
	
	@Override
	public <T> T getValue(ValueAccessor va, Object target) {
		return javaBeanHandler.getValue(va, target);
	}
	
	@Override
	public <T> void setValue(ValueAccessor va, Object target, T value) {
		javaBeanHandler.setValue(va, target, value);
	}
	
	@Override
	public <M> M _instantiateAndSet(Param<M> param) {
		return _instantiateAndSet(defaultRepStrategy, param);
	}
	
	public <M> M _instantiateAndSet(ParamStateRepository currRep, Param<M> param) {
		M newState = instantiate(param.getConfig().getReferredClass());
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
		currRep._set(param, (P)toObject(param.getConfig().getReferredClass(), newState));
	}
	
	/***
	 * 1. get value from rep - could be a composite repository hiding all the write-through cache, 1/2nd/3rd level details
	 */
	@Override
	public <P> P _get(Param<P> param) {
		logit.trace(()->"_get of param: "+param);
		
		Object state = _get(defaultRepStrategy, param);
		if(CollectionUtils.isNotEmpty(param.getConfig().getConverters())) {
			for(ParamConverter converter: param.getConfig().getConverters()) {
				state = converter.serialize(state);
			}
		}
		return (P)state;
	}
	
	public <P> P _get(ParamStateRepository currRep, Param<P> param) {
		if(!param.isMapped())
			return currRep._get(param);
		
		// mapped
		if(param.findIfMapped().getConfig().isMapped()) {
			MappedParamConfig<P, ?> mappedParamConfig = param.findIfMapped().getConfig().findIfMapped();
			
			if(mappedParamConfig.isDetachedWithAutoLoad())
				return mappedParamConfig.getPath().detachedState().cacheState() == Cache.rep_none 
					|| currRep._get(param) == null ? detachedStateRepository._get(param) : currRep._get(param);
		}

		MappedParam<P, ?> mappedParam = param.findIfMapped();
		Param<P> mapsToParam = (Param<P>)mappedParam.getMapsTo();
		
//		if(param.isLeaf())
//			return currRep._get(mapsToParam);
//
//		
//		return currRep._get(param);
		
		if(!mappedParam.requiresConversion()) {
			return mapsToParam.getState();
			//return currRep._get(mapsToParam);
		}
		
		return currRep._get(param);
	}
	
	@Override
	public <P> Action _set(Param<P> param, P newState) {
		logit.trace(()->"_set of param: "+param+" with entityState: "+newState);
		
		return _set(defaultRepStrategy, param, newState);
	}
	
	public <P> Action _set(ParamStateRepository currRep, Param<P> param, P newState) {
		
		newState = (P)toObject(param.getConfig().getReferredClass(), newState);
		
		// unmapped core/view - nested/leaf: set to param
		if(!param.isMapped()) {
			if(newState==null) {
				
				// clear collection elements prior to setting null
				if(param.isCollection()) 
					param.findIfCollection().clear();
				
				// set to null only if parent state is not null
				Param<?> parentParam = Optional.ofNullable(param.getParentModel())
						.map(Model::getAssociatedParam)
						.orElse(null);
				
				if(parentParam==null)
					return currRep._set(param, null);
				
				if(parentParam.getState()!=null)
					return currRep._set(param, null);
				
				return null;
			}
			
			if(param.isLeaf()) {
				return currRep._set(param, newState);	
				
			} else if(param.isCollection()) {
				ListParam<P> listParam = (ListParam<P>)param.findIfCollection();
				//reset collection
				listParam.clear();
				
				//_instantiateAndSet(currRep, param);
				//listParam.getType().getModel().instantiateAndSet();

				if(!(newState instanceof Collection))
					throw new InvalidArgumentException("Collection param with path: "+param.getPath()+" must have argument of type "+Collection.class);
				
				
				Collection<P> state = (Collection<P>)newState;
				// add element parameters
				Optional.ofNullable(state)
					.ifPresent(list->
						list.stream()
							.forEach(listParam::add)
					);
				
				return Action._new;
				
			} else // scenario: when model is mapped to a type, but param is not -- needs to refer to its root param 
			//if(param.getConfig().getType().findIfNested().getModel().isMapped())
			{
				return _setNestedModel(currRep, param, newState);
				
			} 
//			else {
//				// set nested state as is from passed in domain state
//				return currRep._set(param, newState);
//			}
		}
		
		// set to current param
		//currRep._set(param, newState);
		
		MappedParam<P, ?> mappedParam = param.findIfMapped();
		Param<P> mapsToParam = (Param<P>)mappedParam.getMapsTo();
		
		// if param is mapped && requires NO conversion, then use mapsToParam
		if(!mappedParam.requiresConversion()) {
			return mapsToParam.setState(newState);
			
		} else if(param.isCollection()) {
			if(newState==null) {
				param.getParentModel().instantiateOrGet();//ensure mappedFrom model is instantiated
				return mapsToParam.setState(null);
			}
			

			ListParam<P> mappedListParam = (ListParam<P>)param.findIfCollection();
			
			// reset collection
			mappedListParam.clear();
			
			//_instantiateAndSet(currRep, param);
			//mappedListParam.getType().getModel().instantiateAndSet();

			if(!(newState instanceof Collection))
				throw new InvalidArgumentException("Collection param with path: "+param.getPath()+" must have argument of type "+Collection.class);
			
			Collection<P> newColState = (Collection<P>)newState;
			// add element parameters
			Optional.ofNullable(newColState)
				.ifPresent(list->
					list.stream()
						.forEach(mappedListParam::add)
				);
			return Action._new;
			
		} else if(param.isNested()) {
			// mapped nested: ..handling..  <TypeStateAndConfig.Nested<P>>
			return _setNestedModel(currRep, param, newState);
			
		} else if(param.isLeaf()) {
			Object parentModel = param.getParentModel().instantiateOrGet();//ensure mappedFrom model is instantiated
			
			if(CollectionUtils.isNotEmpty(param.getConfig().getConverters())) {
				Collections.reverse(param.getConfig().getConverters());
				Object output = newState;
				for(ParamConverter converter: param.getConfig().getConverters()) {
					output = converter.deserialize(output);
				}
				newState = (P)output;
			}
			return mapsToParam.setState(newState);
			
		} 
		
		throw new UnsupportedScenarioException("Found param: "+param+" with type that is currently not supported. "
				+ "Follows the order: a)Transient b)Collection c)Nested d)Leaf");
	
	}
	
	protected <P> Action _setNestedModel(ParamStateRepository currRep, Param<P> param, P newState) {
		// if param is mapped && requires NO conversion, then use mapsToParam
//		if(param.isMapped() && !param.findIfMapped().requiresConversion()) {
//			Param<P> mapsToParam = (Param<P>)param.findIfMapped().getMapsTo();
//			//return _setNestedModel(currRep, mapsToParam, newState);
//			mapsToParam.setState(newState);
//		}
		
		// ensure model is instantiated
		param.findIfNested().instantiateOrGet();
		
//		if(param.isMapped())
//			param.findIfMapped().getMapsTo().findIfNested().instantiateOrGet();
		
		StateType.Nested<P> nestedType = param.getType().findIfNested(); 
		Model<P> nestedModel = nestedType.getModel();
		if(nestedModel.templateParams().isNullOrEmpty()) return null;
		
		// iterate child params
		
		for(Param<? extends Object> childParam : nestedModel.getParams()) {
			
			//PropertyDescriptor pd = (childParam.isMapped() ? childParam.findIfMapped().getMapsTo() : childParam).getPropertyDescriptor();
			ValueAccessor pd = childParam.getValueAccessor();
			Object childParamState = javaBeanHandler.getValue(pd, newState);
			
			//_set(currRep, (Param<Object>)childParam, childParamState);
			((Param<Object>)childParam).setState(childParamState);
		}
		
		//TODO detect change
		return Action._replace;
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
	
	/**
	 * TODO Rakesh - in progress - try replace this with ConvertUtils...
	 */
	public static <P> Object toObject(Class<?> clazz, P value) {
		if(value != null) {
			if( Boolean.class == clazz || Boolean.TYPE == clazz) return Boolean.parseBoolean(value.toString());
			if( Short.class == clazz || Short.TYPE == clazz) return Short.parseShort(value.toString());
			if( Integer.class == clazz || Integer.TYPE == clazz) return Integer.parseInt(value.toString());
			if( Long.class == clazz || Long.TYPE == clazz) return Long.parseLong(value.toString());
			if( Float.class == clazz || Float.TYPE == clazz) return Float.parseFloat(value.toString());
			if( Double.class == clazz || Double.TYPE == clazz) return Double.parseDouble(value.toString());
		}
	    return value;
	}
	
}
