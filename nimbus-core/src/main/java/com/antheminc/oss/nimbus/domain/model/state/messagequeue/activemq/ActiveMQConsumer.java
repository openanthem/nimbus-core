/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.domain.model.state.messagequeue.activemq;

import org.springframework.jms.annotation.JmsListener;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.channel.messagequeue.MessageQueueCommandDispatcher;
import com.antheminc.oss.nimbus.domain.model.state.messagequeue.MessageQueueConsumer;
import com.antheminc.oss.nimbus.domain.model.state.messagequeue.MessageQueueEvent;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Sandeep Mantha
 * @author Tony Lopez
 */
@Getter
@Setter
@RequiredArgsConstructor
@EnableLoggingInterceptor
public class ActiveMQConsumer implements MessageQueueConsumer {

	public static final JustLogit LOG = new JustLogit(ActiveMQConsumer.class);

	private final MessageQueueCommandDispatcher dispatcher;
	private final ObjectMapper objectMapper;

	@Override
	@JmsListener(destination = "${nimbus.activemq.inbound.name}")
	public void receive(String message) {
		// TODO Create session?
		try {
			getDispatcher().handle(getObjectMapper().readValue(message, MessageQueueEvent.class));
		} catch (Exception e) {
			handleException(message, e);
		}
	}

	/**
	 * <p>Handle exceptions encountered that occur either when reading or during
	 * processing of a message retrieved from the inbound message queue.
	 * @param message the original message payload
	 * @param e the thrown exception
	 */
	protected void handleException(String message, Exception e) throws FrameworkRuntimeException {
		throw new FrameworkRuntimeException("An error occurred while processing the message: " + message
				+ ". Unwrap the exception for more details.", e);
	}
}
