package com.tinthon.ncoa;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.tinthon.ncoa.amqp.MailProducer;
import com.tinthon.ncoa.utils.email.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NcoaApplicationTests {

	public static final String QUEUE_NAME = "mail";

	@Resource
	private EmailService emailService;

	@Autowired
	private MailProducer mailProducer;

	@Autowired
	private Environment env;

	@Test
	public void contextLoads() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("106.14.45.44");
		factory.setUsername("sidney");
		factory.setPassword("root");
		factory.setPort(5672);
		String queue = env.getProperty("rabbitmq.queue");
		try {
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(queue, true, false, false, null);
			String message = "hello";
			channel.basicPublish("", queue, null, message.getBytes());
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void sendMail() {
		String sendTo = "sidney.hang@qq.com";
		String title = "test";
		emailService.sendSimpleMail(sendTo, title);
	}

	@Test
	public void producerTest() {
		mailProducer.sendMessage("hello");
	}

}
