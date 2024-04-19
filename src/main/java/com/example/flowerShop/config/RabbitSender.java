package com.example.flowerShop.config;

import com.example.flowerShop.dto.notification.NotificationDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(NotificationDTO notificationRequestDto) {
        rabbitTemplate.convertAndSend(AMQPConfig.EXCHANGE_NAME, AMQPConfig.ROUTING_KEY, notificationRequestDto);
    }
}