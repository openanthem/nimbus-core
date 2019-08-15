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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.domain.model.state.event.listener.StateAndConfigEventListener;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor
public class MockWebSocketListener {
	
	private final StateAndConfigEventListener mockParamEventListener;
	private List<ModelEvent<Param<?>>> events = new LinkedList<>();
	
	public void createPassThrough() {
		create(event->{
			
		});
	}
	
	@SuppressWarnings("unchecked")
	public void create(Consumer<ModelEvent<Param<?>>> cb) {
		when(mockParamEventListener.shouldAllow(any())).thenReturn(true);
		when(mockParamEventListener.shouldSuppress(any())).thenReturn(false);
		
		when(mockParamEventListener.listen(any(ModelEvent.class)))
		.thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				ModelEvent<Param<?>> event = (ModelEvent<Param<?>>)args[0];
				
				synchronized (events) {
					events.add(event);	
				}
				
				System.out.println("@@@@ Model Event Path: "+ event.getPath());
				cb.accept(event);
				
				return true;
			}
		});
	}
	
	public void flushEvents() {
		this.events.clear();
	}
}
