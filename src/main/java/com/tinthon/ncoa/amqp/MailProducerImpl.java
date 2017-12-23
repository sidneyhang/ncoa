package com.tinthon.ncoa.amqp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class MailProducerImpl implements MailProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    @Override
    public void sendMessage(String message) {

        String exchange = env.getProperty("rabbitmq.exchange");
        String routekey = env.getProperty("rabbitmq.routekey");

        System.out.println(exchange);
        System.out.println(routekey);
        System.out.println(message);
        rabbitTemplate.convertAndSend(exchange, routekey, message);
    }
}
