package com.brilworks.mockup.modules.sms.rest;


import com.brilworks.mockup.modules.sms.dto.SmsRequest;
import com.brilworks.mockup.modules.sms.service.TextGridService;
import com.brilworks.mockup.modules.sms.service.TwilioService;
import com.brilworks.mockup.modules.sms.service.VonageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {

    private final TwilioService twilioService;
    private final TextGridService textgridService;
    private final VonageService vonageService;

    @Autowired
    public SmsController(TwilioService twilioService, TextGridService textGridService, VonageService vonageService) {
        this.twilioService = twilioService;
        this.textgridService = textGridService;
        this.vonageService = vonageService;
    }

    @PostMapping("/twilio/send-sms")
    public String sendTwilioSms(@RequestBody SmsRequest request) {
        twilioService.sendSMS(request.getTo(), request.getMessage());
        return "SMS sent successfully!";
    }

    @PostMapping("/textgrid/send-sms")
    public String sendTextgridSms(@RequestBody SmsRequest request) {
        textgridService.sendSMS(request.getFrom(), request.getTo(), request.getMessage());
        return "SMS sent successfully!";
    }

    @PostMapping("/vonage/send-sms")
    public String sendVonageSms(@RequestBody SmsRequest request){
        vonageService.sendSMS(request.getTo(), request.getMessage());
        return "SMS sent successfully!";
    }

}
