/**
 * 
 */
package com.anthem.nimbus.platform.core.process.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import com.anthem.nimbus.platform.core.process.api.command.CommandTransactionInterceptor;
import com.anthem.nimbus.platform.spec.model.command.MultiExecuteOutput;

/**
 * @author Soham Chakravarti
 *
 */
//@Component
public class WebSocketChannelResponseInterceptor extends ChannelInterceptorAdapter {

	@Autowired CommandTransactionInterceptor interceptor;
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		Object payload = message.getPayload();
		MultiExecuteOutput output = interceptor.handleResponse(payload);
		
		Message<?> convertedMsg = MessageBuilder.withPayload(output).copyHeaders(message.getHeaders()).build();
		return convertedMsg;
	}
}
