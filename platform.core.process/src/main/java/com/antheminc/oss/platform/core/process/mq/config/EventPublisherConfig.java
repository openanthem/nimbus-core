package com.antheminc.oss.platform.core.process.mq.config;

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
