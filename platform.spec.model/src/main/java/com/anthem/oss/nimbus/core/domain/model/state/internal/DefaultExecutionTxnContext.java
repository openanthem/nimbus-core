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
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.InvalidStateException;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString
public class DefaultExecutionTxnContext implements ExecutionTxnContext {
	
	private String id;

	private final BlockingQueue<Notification<Object>> notifications = new LinkedBlockingQueue<>();
	
	private final List<ParamEvent> events = new ArrayList<>();
	
	@Override
	public void addNotification(Notification<Object> notification) {
		try {
			getNotifications().put(notification);
		} catch (InterruptedException ex) {
			throw new FrameworkRuntimeException("Failed to place notification event on queue with value: "+notification, ex);
		}	
	}

	
	@Override
	public void addEvent(ParamEvent event) {
		getEvents().add(event);
	}

	public static class Multi extends DefaultExecutionTxnContext {
		
		private Deque<ExecutionTxnContext> subContextsQueue = new LinkedList<>();
		
		public void push(ExecutionTxnContext next) {
			subContextsQueue.push(next);
		}
		
		public ExecutionTxnContext pop() {
			return subContextsQueue.pop();
		}
		
		
		public boolean isEmpty() {
			return subContextsQueue.isEmpty();
		}
		
		public ExecutionTxnContext peekOrThowEx() {
			if(isEmpty()) 
				throw new InvalidStateException("Txn context not found.");
			
			return subContextsQueue.peek();
		}
		
		@Override
		public void addNotification(Notification<Object> notification) {
			ExecutionTxnContext headTxnCtx = peekOrThowEx();
			headTxnCtx.addNotification(notification);
			
			super.addNotification(notification);
		}
		
		@Override
		public void addEvent(ParamEvent event) {
			ExecutionTxnContext headTxnCtx = peekOrThowEx();
			headTxnCtx.addEvent(event);
			
			super.addEvent(event);
		}
		
	}

	public static void main(String args[]) {
		Multi m = new Multi();
		
		m.push(new DefaultExecutionTxnContext());
		System.out.println("Pushed 1: " + m.subContextsQueue.peek());
		
		m.push(new DefaultExecutionTxnContext());
		System.out.println("Pushed 2: " + m.subContextsQueue.peek());
		
		m.push(new DefaultExecutionTxnContext());
		System.out.println("Pushed 3: " + m.subContextsQueue.peek());
		
		System.out.println("Pulled 1: " +m.pop());
		System.out.println("Peek 1: " + m.subContextsQueue.peek());
		
		System.out.println("Pulled 2: " +m.pop());
		System.out.println("Peek 2: " + m.subContextsQueue.peek());
		
		System.out.println("Pulled 3: " +m.pop());
		System.out.println("Peek 3: " + m.subContextsQueue.peek());
	}

}
