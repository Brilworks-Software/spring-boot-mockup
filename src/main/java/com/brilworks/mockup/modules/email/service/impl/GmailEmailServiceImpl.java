package com.brilworks.mockup.modules.email.service.impl;

import com.brilworks.mockup.modules.email.dto.EmailSuccessDto;
import com.brilworks.mockup.modules.email.service.GmailEmailService;
import com.brilworks.mockup.modules.email.util.EmailUtils;
import jakarta.mail.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class GmailEmailServiceImpl implements GmailEmailService {
    @Autowired
    EmailUtils emailUtils;
    @Override
    public EmailSuccessDto sendEmailUsingGmail(String from, String to) throws MessagingException {

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from,"your third-party application generated password");
            }
        };

        Session session = Session.getInstance(properties,authenticator);

        /* Below method send email using following jakarta dependency

            <dependency>
                <groupId>com.sun.mail</groupId>
                <artifactId>jakarta.mail</artifactId>
                <version>2.0.1</version>
            </dependency>
        */

        emailUtils.sendEmailUsingGmail(session,from,to);

        /* Below method send email using following jakarta dependency

            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-mail</artifactId>
                <version>3.2.1</version>
		    </dependency>
        */
        return emailUtils.sendEmailUsingGmail(from,to);
    }
}
