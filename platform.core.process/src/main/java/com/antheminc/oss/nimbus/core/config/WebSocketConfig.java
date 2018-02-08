package com.antheminc.oss.nimbus.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.ExpiringSession;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<ExpiringSession> {
	
	@Value("${stomp.hostName}")
	private String hostName;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableStompBrokerRelay("/topic/", "/queue/")
			.setRelayHost(hostName) 
			.setRelayPort(61613);
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void configureStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/updates").setAllowedOrigins("*");
	}
	
	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.addDecoratorFactory(webSocketHttpConnectHandlerDecoratorFactory());
	}	
	
	@Bean
	public WebSocketHttpConnectHandlerDecoratorFactory webSocketHttpConnectHandlerDecoratorFactory(){
		return new WebSocketHttpConnectHandlerDecoratorFactory(this.eventPublisher);
	}


}

