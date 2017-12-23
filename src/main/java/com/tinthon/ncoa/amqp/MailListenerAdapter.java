package com.tinthon.ncoa.amqp;

import com.rabbitmq.client.Channel;
import com.tinthon.ncoa.utils.email.EmailService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailListenerAdapter extends MessageListenerAdapter {

    @Autowired
    private EmailService emailService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String body = new String(message.getBody());

        emailService.sendSimpleMail("sidney.hang@qq.com", body);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
