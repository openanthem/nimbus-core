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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.lang.annotation.Annotation;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadGetHandler;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadNewHandler;

/**
 * <p>Abstract (base) event handler implementation for specific event annotation handler(s) to extend from.
 * <p>This class provides abstraction to all the event handler interfaces and would contain common behavior applicable to all event handlers,
 * so specific event annotation handler implementation e.g. {@link ScriptEventHandler} has no impact when new event handlers are added in framework.    
 * 
 * @see StateEventHandlers
 * @author Rakesh Patel
 *
 */
//TODO currently supports state event handlers. other event handlers e.g. config, Txn etc.. will need to be added once supported by framework.
public abstract class AbstractEventHandlerSupport<A extends Annotation>
		implements OnStateLoadHandler<A>, OnStateLoadGetHandler<A>, OnStateLoadNewHandler<A> , OnStateChangeHandler<A> {

	@Override
	public void onStateLoad(A configuredAnnotation, Param<?> param) {
		throw new InvalidConfigException("OnStateLoadHandler is not implemented for "+configuredAnnotation+ " on param "+param);

	}
	
	@Override
	public void onStateLoadGet(A configuredAnnotation, Param<?> param) {
		throw new InvalidConfigException("OnStateLoadGetHandler is not implemented for "+configuredAnnotation+ " on param "+param);
		
	}
	
	@Override
	public void onStateLoadNew(A configuredAnnotation, Param<?> param) {
		throw new InvalidConfigException("OnStateLoadNewHandler is not implemented for "+configuredAnnotation+ " on param "+param);
		
	}
	
	@Override
	public void onStateChange(A configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event) {
		throw new InvalidConfigException("OnStateChangeHandler is not implemented for "+configuredAnnotation+ " on param "+event.getParam());

	}

}
