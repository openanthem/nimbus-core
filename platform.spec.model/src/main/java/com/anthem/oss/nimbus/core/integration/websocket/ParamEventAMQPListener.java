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
package com.anthem.oss.nimbus.core.integration.websocket;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.web.context.request.RequestContextHolder;

import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.AbstractStateAndConfigEventListener;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.MultiExecuteOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.EventOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.internal.CommandTransactionInterceptor;
import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * @author Rakesh Patel
 * 
 */
public class ParamEventAMQPListener extends AbstractStateAndConfigEventListener {

	SimpMessageSendingOperations messageTemplate;

	CommandTransactionInterceptor interceptor;	
	
	//@Autowired
	//WebSocketHttpConnectHandlerDecoratorFactory webSocketHttpConnectHandlerDecoratorFactory;
	
	
	public ParamEventAMQPListener(SimpMessageSendingOperations messageTemplate, CommandTransactionInterceptor interceptor){
		this.messageTemplate = messageTemplate;
		this.interceptor = interceptor;
	}
	
	@Override
	public boolean shouldAllow(EntityState<?> in) {
		final EntityState<?> p;
		if(in.getRootExecution().getAssociatedParam().isLinked()) {
			p = in.getRootExecution().getAssociatedParam().findIfLinked();
		} else {
			p = in;
		}
		
		return super.shouldAllow(p);

	}
	
	@Override
	public boolean listen(ModelEvent<Param<?>> modelEvent) {
		return listenInternal(modelEvent);
	}
	
	protected boolean listenInternal(ModelEvent<Param<?>> result) {
		//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		EventOutput<Param<?>> eventOutput = new EventOutput<Param<?>>(result.getPayload(), Action.getByName(result.getType()), Behavior.$execute);
		ExecuteOutput<EventOutput<Param<?>>> executeOutput = new ExecuteOutput<>();
		executeOutput.setResult(eventOutput);
		
		MultiExecuteOutput multiExecOutput = interceptor.handleResponse(executeOutput);
		String webSocketSessionId = getAssociatedWebSocketSessionId();
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		headerAccessor.setSessionId(webSocketSessionId);		
		//messageTemplate.convertAndSend("/queue/updates", multiExecOutput); // TODO get the destination name from the config server
		messageTemplate.convertAndSendToUser(webSocketSessionId,"/queue/updates", multiExecOutput,headerAccessor.getMessageHeaders(),null);
		return true;
	}
	
	private String getAssociatedWebSocketSessionId(){
		String httpSessionId = RequestContextHolder.getRequestAttributes().getSessionId();
		//return webSocketHttpConnectHandlerDecoratorFactory.getAssociatedWebSocketSessionId(httpSessionId);
		return httpSessionId;
	}

	@Override
	public boolean containsListener(ListenerType listenerType) {
		return ListenerType.websocket == listenerType;
	}

}
