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

import java.util.Arrays;

import org.springframework.core.annotation.AnnotationUtils;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.domain.model.state.event.listener.StateAndConfigEventListener;

/**
 * This is an abstract implementation of event listener of type {@link StateAndConfigEventListener}
 * Concrete implementation MUST extend this class. e.g {@link ParamStateAtomicPersistenceEventListener}
 * 
 * @author Rakesh Patel
 *
 */
public abstract class AbstractStateAndConfigEventListener implements StateAndConfigEventListener {

	@Override
	public boolean shouldAllow(EntityState<?> p) {
		Domain currentDomain = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class);
		
		if(currentDomain == null)
			return false;
		
		Model pModel = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class);
		
		ListenerType listener = Arrays.asList(currentDomain.includeListeners())
				.stream()
				.filter(excludeListener -> !Arrays.asList(pModel.excludeListeners()).contains(excludeListener))
				.filter(includeListener -> containsListener(includeListener))
				.findFirst()
				.orElse(null);
		
		if(listener == null)
			return false;
		
		return true;
	}
	
	@Override
	public abstract boolean listen(ModelEvent<Param<?>> event);
	
	public abstract boolean containsListener(ListenerType listenerType);
}
