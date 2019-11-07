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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.defn.extension.AuditView;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig.MappedModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.domain.model.state.builder.QuadModelBuilder;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;
import com.antheminc.oss.nimbus.domain.model.state.repo.event.RepoEventHandlers.OnPersistHandler;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class AuditViewHandler implements OnPersistHandler<AuditView> {

	private final QuadModelBuilder quadModelBuilder; 
	private final ModelRepositoryFactory repositoryFactory;
	
	public AuditViewHandler(BeanResolverStrategy beanResolver) {
		this.repositoryFactory = beanResolver.get(ModelRepositoryFactory.class);
		this.quadModelBuilder = beanResolver.get(QuadModelBuilder.class);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public void onPersist(AuditView auditView, MappedModelConfig<?, ?> mappedModelConfig, Param<?> coreParam) {
		Command cmd = CommandBuilder
				.withPlatformRelativePath(coreParam.getRootExecution().getRootCommand(), Type.PlatformMarker, 
						"/"+mappedModelConfig.getAlias())
				.setAction(Action._new)
				.getCommand();
		
		Object mappedEntity = instantiateEntity(cmd, mappedModelConfig);
		
		QuadModel<?, ?> q = getQuadModelBuilder().build(cmd, mappedEntity, coreParam);
		Object populatedMappedEntity = q.getView().getLeafState();
		
		ModelRepository mRepo = getRepo(mappedModelConfig);
		mRepo._save(mappedModelConfig.getAlias(), populatedMappedEntity);
	}
	
	protected <T> T instantiateEntity(Command cmd, ModelConfig<T> mConfig) {
		ModelRepository mRepo = getRepo(mConfig);

		return mRepo._new(cmd, mConfig).getState();
	}
	
	protected ModelRepository getRepo(ModelConfig<?> mConfig) {
		ModelRepository mRepo = getRepositoryFactory().get(mConfig);
		
		// non DB
		if(mRepo==null) 
			throw new InvalidConfigException("Expected @Repo on AuditView entity not found with config: "+mConfig);
		
		return mRepo;
	}
}
