package com.tinthon.ncoa.amqp;

public interface MailProducer {
    void sendMessage(String message);
}
