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

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

public class WebSocketConnectHandler<S> implements ApplicationListener<SessionConnectEvent> {
	
	private SimpMessageSendingOperations messagingTemplate;

	public WebSocketConnectHandler(SimpMessageSendingOperations messagingTemplate) {
		super();
		this.messagingTemplate = messagingTemplate;
	}

	public void onApplicationEvent(SessionConnectEvent event) {
		MessageHeaders headers = event.getMessage().getHeaders();
		//Principal user = SimpMessageHeaderAccessor.getUser(headers);

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		//String id2 = headerAccessor.getSessionAttributes().get("SPRING.SESSION.ID").toString();
		  
		String id = SimpMessageHeaderAccessor.getSessionId(headers);
		
		//System.out.println("WS socket connection opened with id: "+id+ " tied to spring session: "+id2);
	}
}
