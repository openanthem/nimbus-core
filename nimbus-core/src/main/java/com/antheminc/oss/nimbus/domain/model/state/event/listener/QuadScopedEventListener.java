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
package com.antheminc.oss.nimbus.domain.model.state.event.listener;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.domain.model.state.internal.AbstractEvent.SuppressMode;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor
public class QuadScopedEventListener implements StateAndConfigEventListener {

	private final List<StateAndConfigEventListener> paramEventListeners;
	
	private SuppressMode suppressMode;
	
	private JustLogit logit = new JustLogit(this.getClass());
	
	
	/**
	 * 
	 */
	@Override
	public synchronized void apply(SuppressMode suppressMode) {
		this.suppressMode = suppressMode;
		logit.trace(()->"suppressMode applied: "+suppressMode);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean listen(ModelEvent<Param<?>> modelEvent) {
		if(CollectionUtils.isEmpty(getParamEventListeners())) return false;
		
		getParamEventListeners()
			.stream()
			.filter(e-> !e.shouldSuppress(getSuppressMode()))
			.filter(e-> e.shouldAllow(modelEvent.getPayload()))
			.forEach(e-> e.listen(modelEvent));
		
		return true;
	}
	
}
