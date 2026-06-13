package com.brilworks.mockup.modules.email.service;

import com.brilworks.mockup.modules.email.dto.EmailSuccessDto;
import jakarta.mail.MessagingException;

public interface GmailEmailService {

    EmailSuccessDto sendEmailUsingGmail(String from, String to) throws MessagingException;
}
