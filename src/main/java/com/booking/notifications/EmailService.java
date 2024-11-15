package com.booking.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Qualifier("email")
public class EmailService implements NotificationService {


    @Autowired
    private JavaMailSender mailSender;// created and configured by spring boot autoconfig

    @Override
    @Async  //may take longbtime better executed by new thread
    public void sendMessage(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("ayoubhass2002@gmail.com");

        mailSender.send(message);
        System.out.println("Email sent successfully");

    }
}
