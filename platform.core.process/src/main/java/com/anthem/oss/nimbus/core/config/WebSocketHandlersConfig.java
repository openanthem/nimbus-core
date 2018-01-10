
package com.anthem.oss.nimbus.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.anthem.oss.nimbus.core.integration.websocket.WebSocketConnectHandler;
import com.anthem.oss.nimbus.core.integration.websocket.WebSocketDisconnectHandler;


//@Configuration
public class WebSocketHandlersConfig<ExpiringSession> {

	@Bean
	public WebSocketConnectHandler<ExpiringSession> webSocketConnectHandler(@Qualifier("brokerMessagingTemplate")SimpMessageSendingOperations messagingTemplate) {
		return new WebSocketConnectHandler<ExpiringSession>(messagingTemplate);
	}

	@Bean
	public WebSocketDisconnectHandler<ExpiringSession> webSocketDisconnectHandler(@Qualifier("brokerMessagingTemplate")SimpMessageSendingOperations messagingTemplate) {
		return new WebSocketDisconnectHandler<ExpiringSession>(messagingTemplate);
	}
}
