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
package com.antheminc.oss.nimbus.domain.model.state.repo.db;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultExecutionContextLoader;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.domain.model.state.internal.AbstractEvent.PersistenceMode;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 * @author Rakesh Patel
 */
@ConfigurationProperties(prefix="nimbus.domain.model.persistence.strategy")
public class ParamStateAtomicPersistenceEventListener extends ParamStatePersistenceEventListener {

	@Getter(value=AccessLevel.PROTECTED)
	ModelRepositoryFactory repoFactory;

	@Getter @Setter
	private PersistenceMode mode = PersistenceMode.ATOMIC;
	
	public ParamStateAtomicPersistenceEventListener(ModelRepositoryFactory repoFactory) {
		this.repoFactory = repoFactory;
	}
	
	@Override
	public boolean shouldAllow(EntityState<?> p) {
		return super.shouldAllow(p) && !p.getRootDomain().getConfig().isRemote() && PersistenceMode.ATOMIC == mode;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean listen(ModelEvent<Param<?>> event) {
		if(DefaultExecutionContextLoader.TH_ACTION.get() == Action._get)
			return false;
		
		Param<?> p = (Param<?>) event.getPayload();
		Model<Object> mRoot = (Model<Object>)p.getRootDomain();
		Param<?> pRoot = mRoot.getAssociatedParam();
		
		Repo repo = mRoot.getConfig().getRepo();
		if(repo == null) {
			throw new InvalidConfigException("Persistent entity must be configured with "+Repo.class.getSimpleName()+" annotation. Not found for root model: "+mRoot);
		} 
			
		ModelRepository modelRepo = getRepoFactory().get(mRoot.getConfig());
		
		if(modelRepo == null) {
			throw new InvalidConfigException("No repository implementation provided for the configured repository :"+repo.value().name()+ " for root model: "+mRoot);
		}
			
		Object coreStateId = mRoot.findParamByPath("/id").getState();
		if(coreStateId == null) {
			modelRepo._new(pRoot.getRootExecution().getRootCommand(), mRoot.getConfig(), mRoot.getState());
			return true;
		}
		
		modelRepo._update(pRoot, pRoot.getState());
		return true;
	}
	
}
