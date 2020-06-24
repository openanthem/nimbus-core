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

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.CollectionUtils;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.defn.event.RepoEvent.OnPersist;
import com.antheminc.oss.nimbus.domain.model.config.EventHandlerConfig;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig.MappedModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.repo.event.RepoEventHandlers.OnPersistHandler;

/**
 * @author Soham Chakravarti
 *
 */
public class RepoEventDelegator {

	private final DomainConfigBuilder domainConfigBuilder;
	
	public RepoEventDelegator(BeanResolverStrategy beanResolver) {
		this.domainConfigBuilder = beanResolver.get(DomainConfigBuilder.class);
	}
	
	/**
	 * Find if param has been annotated with {@linkplain OnPersist} annotation, if found
	 * will delegate handling of the event to an implementation of {@linkplain OnPersistHandler} that is supported by
	 * the annotation that inherits from {@linkplain OnPersist}
	 * 
	 * @param event
	 */
	@TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
	public void handleEvent(RepoParamEvent event) {
		Param<?> eventParam = event.getParam();
		Model<?> domainRootModel = eventParam.getRootDomain();
		
		List<MappedModelConfig<?, ?>> mappedConfigsWithOnPersist = findParamWithOnPersistAnnotation(domainRootModel);
		
		mappedConfigsWithOnPersist.stream()
			.forEach(mappedConfig->{
				
				mappedConfig.getEventHandlerConfig().getOnPersistAnnotations().stream()
					.forEach(a->{
						OnPersistHandler<Annotation> h = mappedConfig.getEventHandlerConfig().getOnPersistHandler(a);
						h.onPersist(a, mappedConfig, domainRootModel.getAssociatedParam());
	
					});
			});
	}
	
	protected List<MappedModelConfig<?, ?>> findParamWithOnPersistAnnotation(Model<?> domainRootModel) {
		ModelConfig<?> m = domainRootModel.getConfig();
		List<MappedModelConfig<?, ?>> mappedConfigs = domainConfigBuilder.findMappedConfigs(m);
		
		if(CollectionUtils.isEmpty(mappedConfigs))
			return Collections.emptyList();
		
		List<MappedModelConfig<?, ?>> mappedConfigsWithOnPersist = mappedConfigs.stream()
			.filter(mappedConfig->{
				return Optional.ofNullable(mappedConfig.getEventHandlerConfig())
					.map(EventHandlerConfig::getOnPersistAnnotations)
					.filter(set->!CollectionUtils.isEmpty(set))
					.isPresent();
			})
			.collect(Collectors.toList());
		
		return mappedConfigsWithOnPersist;
	}
	
}
