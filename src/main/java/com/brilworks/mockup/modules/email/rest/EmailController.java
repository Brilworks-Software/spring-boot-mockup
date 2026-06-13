package com.brilworks.mockup.modules.email.rest;


import com.brilworks.mockup.modules.email.dto.EmailDto;
import com.brilworks.mockup.modules.email.service.impl.AwsSesEmailServiceImpl;
import com.brilworks.mockup.modules.email.service.impl.GmailEmailServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    GmailEmailServiceImpl gmailEmailService;

    @Autowired
    AwsSesEmailServiceImpl awsSesEmailService;

    @GetMapping("/gmail")
    public ResponseEntity<?> sendEmailUsingGmail(@Valid @RequestBody EmailDto emailDto) throws MessagingException {
        return ResponseEntity.status(HttpStatus.OK).body(gmailEmailService.sendEmailUsingGmail(emailDto.getSender(), emailDto.getReceiver()));
    }

    @GetMapping("/aws-ses")
    public ResponseEntity<?> sendEmailUsingAmazonSES(@Valid @RequestBody EmailDto emailDto){
        return ResponseEntity.status(HttpStatus.OK).body(awsSesEmailService.sendEmailUsingAWSSES(emailDto.getSender(), emailDto.getReceiver()));
    }
}
