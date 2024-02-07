package com.ehme.michael.components;

import com.ehme.michael.records.SimpleEmail;
import jakarta.mail.internet.MimeMessage;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailServiceTests {

    @Mock
    private JavaMailSender javaMailSender;

    private EmailService emailService;

    @Value("${personal.email}")
    private String email;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService(javaMailSender);
    }

    @Test
    void testSendEmail() {
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        SimpleEmail simpleEmail = new SimpleEmail("FROM", "SUBJECT", "TO");
        emailService.sendSimpleMessage(simpleEmail);
        Mockito.verify(javaMailSender, Mockito.times(2)).send(captor.capture());
        List<SimpleMailMessage> actualMessages = captor.getAllValues();
        List<SimpleMailMessage> expectedMessages = constructSimpleMessages(simpleEmail);

        assertThat(actualMessages).isEqualTo(expectedMessages);

    }

    private List<SimpleMailMessage> constructSimpleMessages(SimpleEmail simpleEmail) {
        List<SimpleMailMessage> simpleMailMessages = new ArrayList<>(2);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@michaelehme.com");
        message.setTo(email);
        message.setReplyTo(simpleEmail.from());
        message.setSubject(simpleEmail.subject());
        message.setText(simpleEmail.text());
        simpleMailMessages.add(message);


        message = new SimpleMailMessage();
        message.setFrom("no-reply@michaelehme.com");
        message.setTo(simpleEmail.from());
        message.setSubject("Confirmation");
        message.setText("Thank you for your email");
        simpleMailMessages.add(message);

        return simpleMailMessages;
    }





}
