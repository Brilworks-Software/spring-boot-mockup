package com.brilworks.mockup.modules.sms.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;


@Service
public class TextGridService {

    private final String url;
    private final String apiPayload = "{\"body\":\"%s\", \"from\":\"%s\", \"to\":\"%s\"}";

    @Value("${textgrid.authToken}")
    String authToken;

    @Value("${textgrid.accountsid}")
    String accountSid;

    public TextGridService() {
        this.url = "https://api.textgrid.com/2010-04-01/Accounts/%s/Messages.json";
    }

    public void sendSMS(String from, String recipient, String message) {
        try {

            String url = String.format(this.url, this.accountSid);
            String payload = String.format(this.apiPayload, message, from, checkAndUpdateRecipient(recipient));

            String plainAuthToken = this.accountSid + ":" + this.authToken;
            Base64.Encoder encoder = Base64.getEncoder();
            String encodedAuthToken =
                    encoder.encodeToString(plainAuthToken.getBytes());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(encodedAuthToken);

            // Construct the request payload
            //SmsRequest request = new SmsRequest(message, from, recipient);
            HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

            // Make the API call
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            if(response.getStatusCode().equals(HttpStatus.OK))
                System.out.println();

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private String checkAndUpdateRecipient(String recipient) {
        if(recipient.startsWith("+")){
            return recipient;
        }
        return "+" + recipient;
    }

}
