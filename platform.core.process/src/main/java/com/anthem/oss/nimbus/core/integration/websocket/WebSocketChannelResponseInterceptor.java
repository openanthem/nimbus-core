/**
 * 
 */
package com.anthem.oss.nimbus.core.integration.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import com.anthem.oss.nimbus.core.domain.command.execution.CommandTransactionInterceptor;
import com.anthem.oss.nimbus.core.domain.command.execution.MultiExecuteOutput;

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
