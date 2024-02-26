package com.wenthor.configuration;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {
    @Value("${rabbit.queue}")
    private String queueName;
    @Value("${rabbit.exchange}")
    private String exchangeName;
    @Value("${rabbit.routing}")
    private String routingName;

    @Bean
    public Queue queue(){
        return new Queue(queueName);
    }
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(exchangeName);
    }
    @Bean
    public Binding binding(Queue queue, Exchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(routingName).noargs();

    }
    @Bean
    public Jackson2JsonMessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
