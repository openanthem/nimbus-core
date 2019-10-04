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

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.annotation.Transient;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.DefaultExecutionContextLoader;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
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
@ConfigurationProperties(prefix="model.persistence.strategy")
public class ParamStateAtomicPersistenceEventListener extends ParamStatePersistenceEventListener {

	@Getter(value=AccessLevel.PROTECTED)
	ModelRepositoryFactory repoFactory;

	@Getter @Setter
	private PersistenceMode mode;
	
	public ParamStateAtomicPersistenceEventListener(ModelRepositoryFactory repoFactory) {
		this.repoFactory = repoFactory;
	}
	
	@Override
	public boolean shouldAllow(EntityState<?> p) {
		return super.shouldAllow(p) && PersistenceMode.ATOMIC == mode;
	}
	
	@Override
	public boolean listen(ModelEvent<Param<?>> event) {
		if(DefaultExecutionContextLoader.TH_ACTION.get() == Action._get)
			return false;
		
		Param<?> p = (Param<?>) event.getPayload();
		Model<?> rootModel = p.getRootDomain();
		
		Repo repo = rootModel.getConfig().getRepo();
		if(repo == null) {
			throw new InvalidConfigException("Core Persistent entity must be configured with "+Repo.class.getSimpleName()+" annotation. Not found for root model: "+p.getRootExecution());
		} 
			
		ModelRepository modelRepo = getRepoFactory().get(repo);
		if(modelRepo == null) {
			throw new InvalidConfigException("No repository implementation provided for the configured repository :"+repo.value().name()+ " for root model: "+p.getRootExecution());
		}
		
		if(p.getConfig().isTransientData()) {
			return false;
		}
		
		Serializable coreStateId = (Serializable) rootModel.findParamByPath("/id").getState();
		if(coreStateId == null) {
			modelRepo._new((ModelConfig<Object>) rootModel.getConfig(), rootModel.getState());
			return true;
		}
		
		String repoAlias = repo.alias();
		if (StringUtils.isBlank(repoAlias)) {
			repoAlias = rootModel.getConfig().getAlias();
			if (StringUtils.isBlank(repoAlias)) {
				throw new InvalidConfigException("Core Persistent entity must be configured with "
						+ Domain.class.getSimpleName() + " annotation. Not found for root model: " + rootModel);
			}
		}
		
		modelRepo._update(repoAlias, coreStateId, rootModel.getBeanPath(), rootModel.getState());
		return true;
	}

}
