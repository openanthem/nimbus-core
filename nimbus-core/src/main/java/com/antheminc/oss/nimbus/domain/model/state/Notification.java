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

import java.util.List;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.MappedParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @RequiredArgsConstructor @ToString
public class Notification<T> {
	
	@Getter @RequiredArgsConstructor
	public enum ActionType {
		_updateState(Action._update),

		_newModel(Action._new),
		_resetModel(Action._replace),
		
		_newElem(Action._new),
		_deleteElem(Action._delete),
		
		_evalProcess(Action._process)
		;
		final private Action action;
	}
	
	final private Param<T> source;
	final private ActionType actionType;
	final private Param<?> eventParam;
	
	public interface Producer<T> extends Dispatcher<T> {
		public List<MappedParam<?, T>> getEventSubscribers();
		
		public void registerConsumer(MappedParam<?, T> consumer);
		
		public boolean deregisterConsumer(MappedParam<?, T> consumer);
	}
	
	public interface Dispatcher<T> {
		public void emitNotification(Notification<T> event);
	}
	
	public interface Consumer<T> {
		public void handleNotification(Notification<T> event);
	}
}
