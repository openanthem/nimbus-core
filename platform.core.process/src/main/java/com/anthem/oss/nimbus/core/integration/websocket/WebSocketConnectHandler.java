
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
