/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.anthem.nimbus.platform.core.process.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;

//@Configuration
public class EventPublisherConfig {

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setChannelTransacted(true);
        template.setExchange(MessageConfig.EXCHANGE_NAME);
        template.setRoutingKey(MessageConfig.EVNT_ROUTING_KEY);
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        template.setMessageConverter(jsonConverter);
        return template;
    }

    @Bean
    Queue eventQueue(){
        return new Queue(MessageConfig.EVNT_QUEUE_NAME, false);
    }

    @Bean
    TopicExchange eventsExchange(){
        return new TopicExchange(MessageConfig.EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(MessageConfig.EVNT_QUEUE_NAME);
    }
}
