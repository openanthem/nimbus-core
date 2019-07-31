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
package com.antheminc.oss.nimbus.test.domain.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.domain.model.state.event.listener.StateAndConfigEventListener;

/**
 * MockEventListener for testing the emit event scenarios
 * 
 * @author Swetha Vemuri
 *
 */
@Configuration
@ActiveProfiles({ "test", "build" })
public class MockEventListener implements StateAndConfigEventListener {

	private List<ModelEvent<Param<?>>> modelEvent;

	@Override
	public boolean shouldAllow(EntityState<?> p) {
		return true;
	}

	@Override
	public boolean listen(ModelEvent<Param<?>> event) {
		if (modelEvent != null)
			modelEvent.add(event);
		else {
			modelEvent = new ArrayList<>();
			modelEvent.add(event);
		}

		return true;
	}

	public List<ModelEvent<Param<?>>> getModelEvent() {
		return this.modelEvent;
	}

	public void flushEvents() {
		if (getModelEvent() != null) {
			this.modelEvent = null;
		}
	}

}
