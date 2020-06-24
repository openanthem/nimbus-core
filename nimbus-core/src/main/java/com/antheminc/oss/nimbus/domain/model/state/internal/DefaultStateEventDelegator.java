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
package com.antheminc.oss.nimbus.domain.model.state.internal;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.collections.CollectionUtils;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.defn.extension.ChangeLog;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionRuntime;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.domain.model.state.StateEventDelegator;
import com.antheminc.oss.nimbus.domain.model.state.StateEventListener;
import com.antheminc.oss.nimbus.domain.model.state.extension.ChangeLogCommandEventHandler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(of={}) @RequiredArgsConstructor
public class DefaultStateEventDelegator implements StateEventDelegator {
	
	private List<StateEventListener> defaultScopedListeners;
	
	private final EntityStateAspectHandlers provider;
	
	private final ChangeLogCommandEventHandler cmdHandler; 
	
	private static final ThreadLocal<List<StateEventListener>> listenersInThread = new ThreadLocal<List<StateEventListener>>() {
		@Override
	 	protected List<StateEventListener> initialValue() {
			return new ArrayList<>();
		}
	};
	
	public DefaultStateEventDelegator(EntityStateAspectHandlers provider) {
		this.provider = provider;
		this.cmdHandler = provider.getBeanResolver().find(ChangeLogCommandEventHandler.class);
	}

	
	@Override
	public void addTxnScopedListener(StateEventListener listener) {
		listenersInThread.get().add(listener);
	}
	
	@Override
	public boolean removeTxnScopedListener(StateEventListener listener) {
		return listenersInThread.get().remove(listener);
	}
	
	@Override
	public void onStartRuntime(ExecutionRuntime execRt) {
		delegate(l->l.onStartRuntime(execRt));
	}
	
	@Override
	public void onStopRuntime(ExecutionRuntime execRt) {
		delegate(l->l.onStopRuntime(execRt));
	}
	
	@Override
	public void onStartTxn(ExecutionTxnContext txnCtx) {
		delegate(l->l.onStartTxn(txnCtx));
	}

	@Override
	public void onEvent(ExecutionTxnContext txnCtx, ParamEvent event) {
		delegate(l->l.onEvent(txnCtx, event));
	}
	
	@Override
	public void onStopTxn(ExecutionTxnContext txnCtx) {
		Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents = aggregate(txnCtx);
		delegate(l->l.onStopTxn(txnCtx, aggregatedEvents));
	}
	
	@Override
	public void onStartRootCommandExecution(Command cmd) {
		delegate(l->l.onStartRootCommandExecution(cmd));
	}
	
	@Override
	public void onStopRootCommandExecution(Command cmd, ExecutionTxnContext txnCtx) {
		Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents = aggregate(txnCtx);
		if (null != getCmdHandler()) {
			getCmdHandler().handleOnRootStop(new ChangeLog() {
				@Override
				public Class<? extends Annotation> annotationType() {
					return ChangeLog.class;
				}
			}, cmd, aggregatedEvents);
		}
		
		delegate(l->l.onStopRootCommandExecution(cmd, aggregatedEvents));
		
		// reset thread local
		listenersInThread.get().clear();
	}
	
	@Override
	public void onStartCommandExecution(Command cmd) {
		delegate(l->l.onStartCommandExecution(cmd));
	}
	
	@Override
	public void onStopCommandExecution(Command cmd, ExecutionTxnContext txnCtx) {
		Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents = aggregate(txnCtx);
		if (null != getCmdHandler()) {
			getCmdHandler().handleOnSelfStop(new ChangeLog() {
				@Override
				public Class<? extends Annotation> annotationType() {
					return ChangeLog.class;
				}
			}, cmd, aggregatedEvents);
		}
		
		delegate(l->l.onStopCommandExecution(cmd, aggregatedEvents));
	}
	
	private Map<ExecutionModel<?>, List<ParamEvent>> aggregate(ExecutionTxnContext txnCtx) {
		Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents = new HashMap<>();
		
		List<ParamEvent> events = txnCtx.getEvents();
		if(CollectionUtils.isEmpty(events))
			return aggregatedEvents;

		Map<String, Param<?>> unique = new HashMap<>(events.size());
		for(ParamEvent pe : events) {
			//ParamEvent pe = events.poll();
			
			List<ParamEvent> groupedEvents = createOrGet(aggregatedEvents, pe.getParam().getRootExecution());
			
			
			Param<?> colParent = findFirstCollectionNode(pe.getParam());
			ParamEvent resolved = (colParent==null) ? pe : new ParamEvent(pe.getAction(), colParent);

			if(!unique.containsKey(resolved.getParam().getPath())) {
				unique.put(resolved.getParam().getPath(), resolved.getParam());
					
				groupedEvents.add(resolved);
			}

		}
		
		return aggregatedEvents;
	}

	private List<ParamEvent> createOrGet(Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents, ExecutionModel<?> root) {
		root = resolveRoot(root);
		
		if(aggregatedEvents.containsKey(root))
			return aggregatedEvents.get(root);
		
		List<ParamEvent> events = new ArrayList<>();
		aggregatedEvents.put(root, events);
		
		return events;
	}
	
	private ExecutionModel<?> resolveRoot(ExecutionModel<?> root) {
		if(root.getAssociatedParam().isLinked()) 
			return root.getAssociatedParam().findIfLinked().getRootExecution();
		
		return root;
	} 
	
	public static Param<?> findFirstCollectionNode(Param<?> currParam) {
		if(currParam.isCollection())
			return currParam;
		
		if(currParam.getParentModel()==null)
			return null;
		
		if(currParam.getParentModel().getAssociatedParam().isCollection())
			return currParam.getParentModel().getAssociatedParam();
		
		return findFirstCollectionNode(currParam.getParentModel().getAssociatedParam());
	}
	
	private void delegate(Consumer<StateEventListener> cb) {
		nullSafeGetDefault(cb);
		nullSafeGetTxn(cb);
	}
	
	private void nullSafeGetDefault(Consumer<StateEventListener> cb) {
		Optional.ofNullable(getDefaultScopedListeners())
			.ifPresent(list->list.forEach(cb));
	}
	
	private void nullSafeGetTxn(Consumer<StateEventListener> cb) {
		Optional.ofNullable(listenersInThread.get())
			.ifPresent(list->list.forEach(cb));
	}

}
