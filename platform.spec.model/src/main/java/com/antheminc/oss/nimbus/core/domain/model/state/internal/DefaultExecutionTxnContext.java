/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.internal;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.antheminc.oss.nimbus.core.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.core.domain.model.state.InvalidStateException;
import com.antheminc.oss.nimbus.core.domain.model.state.Notification;
import com.antheminc.oss.nimbus.core.domain.model.state.ParamEvent;

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
