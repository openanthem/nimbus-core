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
package com.antheminc.oss.nimbus.domain.model.state.repo.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.event.listener.BulkEventListener;
import com.antheminc.oss.nimbus.domain.model.state.internal.AbstractEvent;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelPersistenceHandler;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
public class ParamStateBulkPersistenceEventListener implements BulkEventListener<AbstractEvent<String,Param<?>>> {

	@Autowired
	ModelRepositoryFactory repoFactory;
	
	@Getter @Setter
	List<ModelEvent<Param<?>>> modelEvents;

	public ParamStateBulkPersistenceEventListener(ModelRepositoryFactory repoFactory) {
		this.repoFactory = repoFactory;
	}

	@Override
	public boolean listen(List<AbstractEvent<String, Param<?>>> events) {
		
		if(CollectionUtils.isEmpty(events))
			return true;
		List<String> domainRootsHandled = new ArrayList<>();
		
		events.stream()
			.filter((event) -> shouldAllow(event.getPayload()))
			.forEach((event) ->  {
				if(!domainRootsHandled.contains(event.getPayload().getRootDomain().getConfig().getAlias())) {
					Repo repo = event.getPayload().getRootDomain().getConfig().getRepo();
					
					if(repo != null) {
						ModelPersistenceHandler handler = repoFactory.getHandler(repo);
						ModelEvent<Param<?>> e = new ModelEvent<Param<?>>(Action.getByName(event.getType()), event.getPayload().getRootDomain().getPath(), event.getPayload().getRootDomain().getAssociatedParam());
						List<ModelEvent<Param<?>>> tempEvents = new ArrayList<>();
						tempEvents.add(e);
						handler.handle(tempEvents);
					}
						
				}
			});
		
		
		return false;
	}
	
	private boolean shouldAllow(EntityState<?> p) {
		Domain rootDomain = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class);
		if(rootDomain == null) 
			return false;
		
		Model pModel = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class);
		
		ListenerType includeListener = Arrays.asList(rootDomain.includeListeners()).stream()
											.filter((listener) -> !Arrays.asList(pModel.excludeListeners()).contains(listener))
											.filter((listenerType) -> listenerType == ListenerType.persistence)
											.findFirst()
											.orElse(null);
		
		if(includeListener == null)
			return false;
		
		return true;
	}

}
