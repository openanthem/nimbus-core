package com.anthem.nimbus.platform.core.process.websocket.handler;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public class WebSocketDisconnectHandler<S> implements ApplicationListener<SessionDisconnectEvent> {
	private SimpMessageSendingOperations messagingTemplate;

	public WebSocketDisconnectHandler(SimpMessageSendingOperations messagingTemplate) {
		super();
		this.messagingTemplate = messagingTemplate;
		
	}

	public void onApplicationEvent(SessionDisconnectEvent event) {
		String id = event.getSessionId();
		if (id == null) {
			return;
		}
		
		System.out.println("WS session disconnected: "+id);

	}
}
