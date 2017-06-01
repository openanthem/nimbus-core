package com.anthem.oss.nimbus.core.config;

import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * Override security aspects of web socket
 * 
 * @author Rohit Bajaj
 */
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages
		.simpDestMatchers("/updates").permitAll()
		.simpMessageDestMatchers("/app/**").permitAll()
		.simpTypeMatchers(MESSAGE).permitAll()
		.anyMessage().permitAll();
	}
	
	/**
     * Disables CSRF for Websockets.
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
