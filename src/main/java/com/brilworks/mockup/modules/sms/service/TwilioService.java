package com.brilworks.mockup.modules.sms.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth.token}")
    private String AUTH_TOKEN;

    @Value("${twilio.phone.number}")
    private String TWILIO_PHONE_NUMBER;

    public void sendSMS(String to, String message) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER),
                message
        ).create();
    }



}
