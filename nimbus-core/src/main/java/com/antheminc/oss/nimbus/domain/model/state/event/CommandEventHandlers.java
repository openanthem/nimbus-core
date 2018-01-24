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
package com.antheminc.oss.nimbus.domain.model.state.event;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import com.antheminc.oss.nimbus.domain.EventHandler;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.defn.event.CommandEvent.OnRootExecute;
import com.antheminc.oss.nimbus.domain.defn.event.CommandEvent.OnSelfExecute;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ExecutionModel;

/**
 * @author Soham Chakravarti
 *
 */
public final class CommandEventHandlers {

	@EventHandler(OnRootExecute.class)
	public interface OnRootExecuteHandler<A extends Annotation> {
		
		public void handleOnStart(A configuredAnnotation, Command cmd);
		
		public void handleOnStop(A configuredAnnotation, Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents);	
	}
	
	
	@EventHandler(OnSelfExecute.class)
	public interface OnSelfExecuteHandler<A extends Annotation> {
		
		public void handleOnStart(A configuredAnnotation, Command cmd);
		
		public void handleOnStop(A configuredAnnotation, Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents);	
	} 
}
