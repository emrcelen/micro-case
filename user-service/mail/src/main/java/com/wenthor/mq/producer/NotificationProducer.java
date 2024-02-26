package com.wenthor.mq.producer;

import com.wenthor.model.Notification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationProducer {
    @Value("${rabbit.routing}")
    private String routingName;
    @Value("${rabbit.exchange}")
    private String exchangeName;

    private final RabbitTemplate template;

    public NotificationProducer(RabbitTemplate template) {
        this.template = template;
    }

    public void sendToQueue(Notification notification){
        template.convertAndSend(exchangeName,routingName,notification);
    }
}
