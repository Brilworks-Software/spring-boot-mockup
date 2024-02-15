package com.brilworks.mockup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        // Configure mailSender properties like host, port, username, password, etc.
        mailSender.setHost("your_mail_server_host");
        mailSender.setPort(25);
        mailSender.setUsername("your_username");
        mailSender.setPassword("your_password");
        return mailSender;
    }
}
