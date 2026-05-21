package com.org.easyticket.service.mail.impl;

import com.org.easyticket.service.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.from.address}")
    private String fromAddress;

    @Override
    public void sendMailService(String customerName, String emailSendTo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(emailSendTo);
        message.setSubject("SMTP Test");
        message.setText("This is a test mail from Spring Boot.");

        mailSender.send(message);

        System.out.println("Mail sent successfully!");
    }
}