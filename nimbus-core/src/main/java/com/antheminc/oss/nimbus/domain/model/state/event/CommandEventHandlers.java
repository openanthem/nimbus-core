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
package com.antheminc.oss.nimbus.domain.model.state.event;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import com.antheminc.oss.nimbus.domain.EventHandler;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.defn.event.CommandEvent.OnRootCommandExecute;
import com.antheminc.oss.nimbus.domain.defn.event.CommandEvent.OnSelfCommandExecute;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;

/**
 * @author Soham Chakravarti
 *
 */
public final class CommandEventHandlers {

	@EventHandler(OnRootCommandExecute.class)
	public interface OnRootCommandExecuteHandler<A extends Annotation> {
		
		public void handleOnRootStart(A configuredAnnotation, Command cmd);
		
		public void handleOnRootStop(A configuredAnnotation, Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents);	
	}
	
	
	@EventHandler(OnSelfCommandExecute.class)
	public interface OnSelfCommandExecuteHandler<A extends Annotation> {
		
		public void handleOnSelfStart(A configuredAnnotation, Command cmd);
		
		public void handleOnSelfStop(A configuredAnnotation, Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents);	
	} 
}
