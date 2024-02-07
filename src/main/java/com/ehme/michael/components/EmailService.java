package com.ehme.michael.components;

import com.ehme.michael.records.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${personal.email}")
    private String email;

    @Autowired
    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(SimpleEmail simpleEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@michaelehme.com");
        message.setTo(email);
        message.setReplyTo(simpleEmail.from());
        message.setSubject(simpleEmail.subject());
        message.setText(simpleEmail.text());
        javaMailSender.send(message);

        message = new SimpleMailMessage();
        message.setFrom("no-reply@michaelehme.com");
        message.setTo(simpleEmail.from());
        message.setSubject("Confirmation");
        message.setText("Thank you for your email");
        javaMailSender.send(message);
    }


}
