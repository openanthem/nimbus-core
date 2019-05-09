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

import java.util.Hashtable;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import com.antheminc.oss.nimbus.app.extension.config.properties.ActiveMQConfigurationProperties;
import com.antheminc.oss.nimbus.channel.messagequeue.MessageQueueCommandDispatcher;
import com.antheminc.oss.nimbus.domain.model.state.messagequeue.activemq.ActiveMQConsumer;
import com.antheminc.oss.nimbus.domain.model.state.messagequeue.activemq.ActiveMQParamStateEventListener;
import com.antheminc.oss.nimbus.domain.model.state.messagequeue.activemq.ActiveMQPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Sandeep Mantha
 * @author Tony Lopez
 */
@Configuration
@EnableJms
@ConditionalOnProperty("nimbus.activemq.broker-url")
public class DefaultActiveMQConfig {

	@Bean
	@ConditionalOnProperty(name = "nimbus.activemq.inbound.name")
	public ActiveMQConsumer activeMQConsumer(MessageQueueCommandDispatcher dispatcher, ObjectMapper om) {
		return new ActiveMQConsumer(dispatcher, om);
	}

	@Bean(name = "default.paramStateMqEventListener")
	@ConditionalOnBean(value = ActiveMQPublisher.class)
	public ActiveMQParamStateEventListener activeMQParamStateEventListener(ActiveMQPublisher publisher) {
		return new ActiveMQParamStateEventListener(publisher);
	}

	@Bean
	@ConditionalOnProperty(name = "nimbus.activemq.outbound.name")
	public ActiveMQPublisher activeMQPublisher(JmsTemplate jmsTemplate, ObjectMapper om, ActiveMQConfigurationProperties config) {
		return new ActiveMQPublisher(jmsTemplate, om, config);
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory(ConnectionFactory connectionFactory) {
		return new CachingConnectionFactory(connectionFactory);
	}

	@Bean
	public ConnectionFactory connectionFactory(ActiveMQConfigurationProperties properties) throws NamingException {
		if (properties.getBrokerUrl().startsWith("amqp")) {
			// Use JNDI to specify the AMQP endpoint
			Hashtable<Object, Object> env = new Hashtable<Object, Object>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.qpid.jms.jndi.JmsInitialContextFactory");
			env.put("connectionfactory.factoryLookup", properties.getBrokerUrl());
			javax.naming.Context context = new javax.naming.InitialContext(env);

			// Create a connection factory.
			JmsConnectionFactory connectionFactory = (JmsConnectionFactory) context.lookup("factoryLookup");
			connectionFactory.setUsername(properties.getUser());
			connectionFactory.setPassword(properties.getPassword());
			return connectionFactory;
		} else {
			ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
			activeMQConnectionFactory.setBrokerURL(properties.getBrokerUrl());
			activeMQConnectionFactory.setUserName(properties.getUser());
			activeMQConnectionFactory.setPassword(properties.getPassword());
			return activeMQConnectionFactory;
		}
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}

	@Bean
	public JmsTemplate jmsTemplate(CachingConnectionFactory cachingConnectionFactory) {
		return new JmsTemplate(cachingConnectionFactory);
	}
}
