package com.bootcampproject.bootcamp_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    private ExecutorService executorService;

    public EmailService() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void sendEmailAsync(String to, String subject, String text) {
        executorService.submit(() -> {
            sendEmailAsync(to, subject, text);
        });
    }

    public void sendEmailInSync(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("projectfirst96@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
