package com.brilworks.mockup.modules.email.util;


import com.brilworks.mockup.exceptions.EmailNotSentException;
import com.brilworks.mockup.modules.email.dto.EmailSuccessDto;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;


@Service
public class EmailUtils {
    private static final Logger logger = LoggerFactory.getLogger(EmailUtils.class);
    private final JavaMailSender javaMailSender;

    public EmailUtils(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public EmailSuccessDto sendEmailUsingGmail(Session session, String from, String to){
        try {
                MimeMessage mimeMessage = new MimeMessage(session);

                mimeMessage.setFrom(from);

                mimeMessage.setRecipients(Message.RecipientType.TO,to);

                mimeMessage.setSubject("With localhost smtp server");

                mimeMessage.setText("This mail is from "+from);

                logger.info("Email sent process start.");

                Transport.send(mimeMessage);

                logger.info("Mail sent successfully !");

        } catch (Exception e) {
                logger.error("Something went wrong can't send email error is : {}",e.getMessage());
                throw new EmailNotSentException(HttpStatus.INTERNAL_SERVER_ERROR.value(),String.format("Something went wrong ! Email not sent to : %s",to));
        }
            return new EmailSuccessDto(HttpStatus.OK.value(),"Email Sent Successfully !");
    }

    public EmailSuccessDto sendEmailUsingGmail(String from , String to){
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setFrom(from);

            simpleMailMessage.setTo(to);

            simpleMailMessage.setSubject("Gmail SMTP server");

            simpleMailMessage.setText("Hello, Gmail here!");

            logger.info("Email sent process start.");

            javaMailSender.send(simpleMailMessage);

            logger.info("Email sent successfully to : {}",to);

        } catch (MailException e){
            logger.error("Something went wrong while sending email to : {} . Error is : {} ",to,e.getMessage());
            throw new EmailNotSentException(HttpStatus.INTERNAL_SERVER_ERROR.value(), String.format("Email not sent to %s",to));
        }
        logger.info("Mail Sent successfully to : "+to);
        return new EmailSuccessDto(HttpStatus.OK.value(), "Email sent successfully to : "+ to);
    }


    public EmailSuccessDto sendEmailUsingAWSSES(SesClient sesClient, String sender, String receiver, String subject, String body){

        Destination destination = Destination.builder()
                .toAddresses(receiver)
                .build();

        Content emailSubject = Content.builder()
                .data(subject)
                .build();

        Content html = Content.builder()
                .data(body)
                .build();

        Body emailBody = Body.builder()
                .html(html)
                .build();

        software.amazon.awssdk.services.ses.model.Message awsMessage = software.amazon.awssdk.services.ses.model.Message
                .builder()
                .subject(emailSubject)
                .body(emailBody)
                .build();

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .destination(destination)
                .message(awsMessage)
                .source(sender)
                .build();

        logger.info("process starts to send email through Amazon ses");

        try {
            sesClient.sendEmail(sendEmailRequest);
            logger.info("Email sent successfully using amazon ses to {}",receiver);
        }
        catch (SesException e){
            logger.error("Something went wrong while sending email using amazon ses, Error occurred in service : {} and error is {}"
                    ,e.awsErrorDetails().serviceName(),e.awsErrorDetails().errorMessage());
            throw new EmailNotSentException(HttpStatus.INTERNAL_SERVER_ERROR.value(),String.format("Something went wrong ! Email not sent to : %s",receiver));
        }
        return new EmailSuccessDto(HttpStatus.OK.value(),"Email Sent Successfully !");
    }
}
