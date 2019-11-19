/**
 *  Copyright 2016-2019 the original author or authors.
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

import java.util.Arrays;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.entity.lock.DefaultDomainLockStrategy;
import com.antheminc.oss.nimbus.entity.lock.DomainEntityLockStrategy;

/**
 * @author Sandeep Mantha
 *
 */
public class DefaultParamStateRepositoryStrategy implements ParamStateRepository{

	private ParamStateRepository session;

	private ParamStateRepository local;

	private DomainEntityLockStrategy domainEntityLockStrategy;
	
	public DefaultParamStateRepositoryStrategy(ParamStateRepository local, BeanResolverStrategy beanResolver) {
		this.local = local;
		this.domainEntityLockStrategy = beanResolver.get(DefaultDomainLockStrategy.class);
	}
	/**
	 * Local is always kept, but follows behind cache if configured.
	 * 
	 * 1. If cache=true, then retrieve state from cache AND set to local before returning if local state is different
	 * 2. If cache=false, then 
	 */
	@Override
	public <P> P _get(Param<P> param) {	
		if(param.isDomainRoot()) {
			if(param.getRootExecution().getRootCommand().getRefId(Type.DomainAlias)!= null && param.getRootDomain().getConfig().getLock() != null) {
				domainEntityLockStrategy.evalAndapply(param);
			}
		}
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

	@Override
	public String toString() {
		return "Default Strategy";
	}
	
	public static <P> Action _equals(P newState, P currState) {
		boolean isEqual;
		//00
		if(newState==null && currState==null) return null;
		
		//01
		if(newState==null && currState!=null) return Action._delete;
		
		//10
		if(newState!=null && currState==null) return Action._new;
		
		//11
		if(currState instanceof Object[] && newState instanceof Object[]) {
			isEqual = Arrays.equals((Object[]) currState, (Object[]) newState); 
		} else {
			isEqual = currState.equals(newState);
		}
	
		return isEqual ? null : Action._update;
	}
}