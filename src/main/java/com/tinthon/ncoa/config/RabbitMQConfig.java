package com.tinthon.ncoa.config;

import com.rabbitmq.client.ConnectionFactory;
import com.tinthon.ncoa.amqp.MailListenerAdapter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackages = {"com.tinthon.ncoa"})
public class RabbitMQConfig {

    @Autowired
    private Environment env;

    @Bean
    public ConnectionFactory connectionFactory() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(env.getProperty("spring.rabbitmq.host"));
        factory.setUsername(env.getProperty("spring.rabbitmq.username"));
        factory.setPassword(env.getProperty("spring.rabbitmq.password"));
        factory.setPort(Integer.parseInt(env.getProperty("spring.rabbitmq.port")));
        return factory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory(ConnectionFactory connectionFactory) {
        return new CachingConnectionFactory(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory cachingConnectionFactory) throws Exception {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }

    @Bean
    public AmqpAdmin amqpAdmin(CachingConnectionFactory cachingConnectionFactory) {
        return new RabbitAdmin(cachingConnectionFactory);
    }


    @Bean
    Queue queue() {
        String name = env.getProperty("rabbitmq.queue");
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new Queue(name, durable, exclusive, autoDelete);
    }

    @Bean
    TopicExchange exchange() {
        String name = env.getProperty("rabbitmq.exchange");
        // 是否持久化
        boolean durable = true;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new TopicExchange(name, durable, autoDelete);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        String routekey = env.getProperty("rabbitmq.routekey");
        return BindingBuilder.bind(queue).to(exchange).with(routekey);
    }


    @Bean
    public SimpleMessageListenerContainer listenerContainer(MailListenerAdapter mailListenerAdapter,
                                                            CachingConnectionFactory cachingConnectionFactory) throws Exception {
        String queueName = env.getProperty("rabbitmq.queue");

        SimpleMessageListenerContainer simpleMessageListenerContainer =
                new SimpleMessageListenerContainer(cachingConnectionFactory);
        simpleMessageListenerContainer.setQueueNames(queueName);
        simpleMessageListenerContainer.setMessageListener(mailListenerAdapter);
        // 设置手动 ACK
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return simpleMessageListenerContainer;
    }

}
