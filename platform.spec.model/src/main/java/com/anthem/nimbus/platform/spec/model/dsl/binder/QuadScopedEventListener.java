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
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.internal.AbstractEvent.SuppressMode;
import com.anthem.oss.nimbus.core.spec.contract.event.StateAndConfigEventListener;
import com.anthem.oss.nimbus.core.util.JustLogit;

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
