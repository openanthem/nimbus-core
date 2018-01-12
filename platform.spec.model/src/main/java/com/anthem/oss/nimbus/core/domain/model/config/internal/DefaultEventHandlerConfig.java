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
package com.anthem.oss.nimbus.core.domain.model.config.internal;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.MapUtils;

import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.config.EventHandlerConfig;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class DefaultEventHandlerConfig implements EventHandlerConfig {

	private Map<Annotation, OnStateLoadHandler<Annotation>> onStateLoadHandlers;
	private Map<Annotation, OnStateChangeHandler<Annotation>> onStateChangeHandlers;
	
	public boolean isEmpty() {
		return MapUtils.isEmpty(getOnStateLoadHandlers()) &&
				MapUtils.isEmpty(getOnStateChangeHandlers());
 	}
	
	
	@Override
	public Set<Annotation> getOnStateLoadAnnotations() {
		return Optional.ofNullable(getOnStateLoadHandlers()).map(Map::keySet).orElse(null);
	}
	
	public void add(Annotation a, OnStateLoadHandler<Annotation> handler) {
		Optional.ofNullable(getOnStateLoadHandlers()).orElseGet(()->{
			setOnStateLoadHandlers(new HashMap<>());
			return getOnStateLoadHandlers();
		}).put(a, handler);
	}
	
	@Override
	public Optional<OnStateLoadHandler<Annotation>> findOnStateLoadHandler(Annotation a) {
		return Optional.ofNullable(getOnStateLoadHandlers()).map(handlers->handlers.get(a));
	}
	
	@Override
	public OnStateLoadHandler<Annotation> getOnStateLoadHandler(Annotation a) throws InvalidConfigException {
		return findOnStateLoadHandler(a).orElseThrow(()->getEx(a));
	}
	
	
	@Override
	public Set<Annotation> getOnStateChangeAnnotations() {
		return Optional.ofNullable(getOnStateChangeHandlers()).map(Map::keySet).orElse(null);
	}
	
	public void add(Annotation a, OnStateChangeHandler<Annotation> handler) {
		Optional.ofNullable(getOnStateChangeHandlers()).orElseGet(()->{
			setOnStateChangeHandlers(new HashMap<>());
			return getOnStateChangeHandlers();
		}).put(a, handler);
	}
	
	@Override
	public Optional<OnStateChangeHandler<Annotation>> findOnStateChangeHandler(Annotation a) {
		return Optional.ofNullable(getOnStateChangeHandlers()).map(handlers->handlers.get(a));
	}
	
	@Override
	public OnStateChangeHandler<Annotation> getOnStateChangeHandler(Annotation a) throws InvalidConfigException {
		return findOnStateChangeHandler(a).orElseThrow(()->getEx(a));
	}
	
	
	private InvalidConfigException getEx(Annotation a) {
		return new InvalidConfigException("Expected event handler for annotation: "+a+ " not found");
	}
}
