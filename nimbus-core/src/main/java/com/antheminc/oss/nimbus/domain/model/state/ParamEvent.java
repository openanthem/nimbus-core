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
package com.antheminc.oss.nimbus.domain.model.state;

import java.time.Instant;
import java.util.Arrays;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @AllArgsConstructor
@ToString
public class ParamEvent {

	private Action action;
	
	private Param<?> param;
	
	@JsonIgnore
	private final Instant createdInstant = Instant.now();
	
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;
		
		if(!ParamEvent.class.isInstance(obj))
			return false;
		
		ParamEvent other = ParamEvent.class.cast(obj);
		if(this==other)
			return true;
		
		return StringUtils.equals(this.getParam().getPath(), other.getParam().getPath());
	}
	
	@Override
	public int hashCode() {
		return getParam().getPath().hashCode();
	}
}
