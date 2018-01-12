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
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import java.util.Arrays;

import org.springframework.core.annotation.AnnotationUtils;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.spec.contract.event.StateAndConfigEventListener;

/**
 * This is an abstract implementation of persistence event listener of type {@link StateAndConfigEventListener}
 * Concrete persistence listener implementation MUST extend this class. e.g {@link ParamStateAtomicPersistenceEventListener}
 * 
 * @author Rakesh Patel
 *
 */
public abstract class ParamStatePersistenceEventListener extends AbstractStateAndConfigEventListener {

	@Override
	public boolean containsListener(ListenerType listenerType) {
		return ListenerType.persistence == listenerType;
	}
	
}
