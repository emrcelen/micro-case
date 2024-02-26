package com.wenthor.mq.listener;

import com.wenthor.model.Notification;
import com.wenthor.service.MailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {
    private final MailService mailService;

    public NotificationListener(MailService mailService) {
        this.mailService = mailService;
    }

    @RabbitListener(queues = "${rabbit.queue}")
    public void handleMessage(Notification notification){
        this.mailService.sendMail(notification);
    }
}
