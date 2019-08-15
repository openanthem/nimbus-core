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

import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.app.extension.config.properties.ActiveMQConfigurationProperties;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.messagequeue.MessageQueuePublisher;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Sandeep Mantha
 */
@Getter
@Setter
@RequiredArgsConstructor
@EnableLoggingInterceptor
public class ActiveMQPublisher implements MessageQueuePublisher {

	private final JmsTemplate jmsTemplate;
	private final ObjectMapper objectMapper;
	private final ActiveMQConfigurationProperties config;

	@Override
	public boolean send(final Param<?> param) {
		String sMessage;
		try {
			sMessage = getObjectMapper().writeValueAsString(param);
		} catch (JsonProcessingException e) {
			throw new FrameworkRuntimeException("Failed to convert message to string. Message: " + param);
		}
		
		try {
			getJmsTemplate().convertAndSend(getConfig().getOutbound().getName(), sMessage);
			return true;
		} catch(JmsException e) {
			throw new FrameworkRuntimeException("Failed to write message to queue \"" + getConfig().getOutbound().getName() + "\" with payoad: " + sMessage);
		}
	}
}
