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
package com.antheminc.oss.nimbus.app.extension.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import com.antheminc.oss.nimbus.channel.web.WebCommandDispatcher;
import com.antheminc.oss.nimbus.domain.model.state.queue.ParamStateMQEventListener;
import com.antheminc.oss.nimbus.integration.mq.ActiveMQConsumer;
import com.antheminc.oss.nimbus.integration.mq.ActiveMQPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Sandeep Mantha
 * @author Tony Lopez
 */
@Configuration
@EnableJms
@ConditionalOnProperty(name = "spring.activemq.broker-url")
public class DefaultActiveMQConfig {

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;

	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		return activeMQConnectionFactory;
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		return new CachingConnectionFactory(activeMQConnectionFactory());
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(activeMQConnectionFactory());
		return factory;
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		return new JmsTemplate(cachingConnectionFactory());
	}

	@Bean
	@ConditionalOnProperty(name = "activemq.inbound.channel")
	public ActiveMQConsumer mqconsumer(WebCommandDispatcher dispatcher, ObjectMapper om) {
		return new ActiveMQConsumer(dispatcher, om);
	}

	@Bean(name = "default.paramStateMqEventListener")
	@ConditionalOnBean(value = ActiveMQPublisher.class)
	public ParamStateMQEventListener paramStateMQEventListener(ActiveMQPublisher publisher) {
		return new ParamStateMQEventListener(publisher);
	}

	@Bean
	@ConditionalOnProperty(name = "activemq.outbound.channel")
	public ActiveMQPublisher publisher(ObjectMapper om) {
		return new ActiveMQPublisher(jmsTemplate(), om);
	}
}
