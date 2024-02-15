package com.brilworks.mockup.modules.sms.service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VonageService {

    @Value("${vonage.api.key}")
    String apiKey;

    @Value("${vonage.api.secret}")
    String apiSecret;

    public void sendSMS(String to, String messageBody){
        VonageClient client = VonageClient.builder().apiKey(apiKey).apiSecret(apiSecret).build();

        TextMessage message = new TextMessage("Vonage SMS Service",
                to,
                messageBody
        );

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }
}
