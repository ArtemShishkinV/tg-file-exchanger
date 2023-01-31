package com.shishkin.service.impl;

import com.shishkin.model.EmailMessage;
import com.shishkin.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public void send(EmailMessage message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(message.to());
        mailMessage.setSubject(message.subject());
        mailMessage.setText(message.text());

        javaMailSender.send(mailMessage);
    }
}
