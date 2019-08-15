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
package com.antheminc.oss.nimbus.domain.model.state.repo.ws;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultActionExecutorGet;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultExecutionContextLoader;
import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.domain.model.state.event.listener.StateAndConfigEventListener;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Soham Chakravarti
 * @author Rakesh Patel
 */
public class ParamStateAtomicRemotePersistenceEventListener implements StateAndConfigEventListener {

	@Getter(value=AccessLevel.PROTECTED)
	ModelRepositoryFactory repoFactory;
	
	public ParamStateAtomicRemotePersistenceEventListener(ModelRepositoryFactory repoFactory) {
		this.repoFactory = repoFactory;
	}
	
	@Override
	public boolean shouldAllow(EntityState<?> p) {
		return p.getRootDomain().getConfig().isRemote();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean listen(ModelEvent<Param<?>> event) {
		if(DefaultExecutionContextLoader.TH_ACTION.get() == Action._get)
			return false;
		
		Param<?> p = (Param<?>) event.getPayload();
		Model<Object> mRoot = (Model<Object>)p.getRootDomain();
		Param<?> pRoot = mRoot.getAssociatedParam();
		
		ModelRepository remoteRepo = getRepoFactory().get(mRoot.getConfig());
	
		if(remoteRepo == null) {
			throw new InvalidConfigException("No remote repository implementation provided for the configured param :"+p+" and root model: "+mRoot);
		}
			
		remoteRepo._update(pRoot, pRoot.getState());
		
		return true;
	}
	
}
