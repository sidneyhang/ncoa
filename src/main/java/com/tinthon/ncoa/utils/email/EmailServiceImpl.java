package com.tinthon.ncoa.utils.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendSimpleMail(String sendTo, String title) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
            messageHelper.setFrom("sidney_hang@163.com");
            messageHelper.setTo(sendTo);
            messageHelper.setSubject(title);
            Context context = new Context();
            context.setVariable("name", "yang");

            String content = templateEngine.process("email", context);
            messageHelper.setText(content, true);
            mailSender.send(mailMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }
}
