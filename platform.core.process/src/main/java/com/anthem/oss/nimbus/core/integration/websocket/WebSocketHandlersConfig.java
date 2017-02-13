
package com.anthem.oss.nimbus.core.integration.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;


@Configuration
public class WebSocketHandlersConfig<ExpiringSession> {

	@Bean
	public WebSocketConnectHandler<ExpiringSession> webSocketConnectHandler(SimpMessageSendingOperations messagingTemplate) {
		return new WebSocketConnectHandler<ExpiringSession>(messagingTemplate);
	}

	@Bean
	public WebSocketDisconnectHandler<ExpiringSession> webSocketDisconnectHandler(SimpMessageSendingOperations messagingTemplate) {
		return new WebSocketDisconnectHandler<ExpiringSession>(messagingTemplate);
	}
}
