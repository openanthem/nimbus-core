/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.util.Arrays;

import org.springframework.core.annotation.AnnotationUtils;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Soham Chakravarti
 *
 */
@Data @AllArgsConstructor @EqualsAndHashCode(of="param")
public class ParamEvent {

	private Action action;
	
	private Param<?> param;
	
	public boolean shouldAllow() {
		final EntityState<?> p;
		if(param.getRootExecution().getAssociatedParam().isLinked()) {
			p = param.getRootExecution().getAssociatedParam().findIfLinked();
		} else {
			p = param;
		}
		
		return shouldAllow(p);
	}
	
	public boolean shouldAllow(EntityState<?> p) {
		
		Domain currentDomain = AnnotationUtils.findAnnotation(p.getConfig().getReferredClass(), Domain.class);
		
		if(currentDomain == null)
			currentDomain = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class);
		
		if(currentDomain == null)
			return false;
		
		Model pModel = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class);
		
		ListenerType includeListener = Arrays.asList(currentDomain.includeListeners())
				.stream()
				.filter((listener) -> !Arrays.asList(pModel.excludeListeners()).contains(listener))
				.filter((listenerType) -> containsListener(listenerType))
				.findFirst()
				.orElse(null);
		
		if(includeListener == null)
			return false;
		
		return true;
	}
	
	// TODO move this to EventManager
	public boolean containsListener(ListenerType listenerType) {
		return ListenerType.websocket == listenerType;
	}
}
