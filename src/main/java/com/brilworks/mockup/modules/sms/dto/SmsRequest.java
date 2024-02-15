package com.brilworks.mockup.modules.sms.dto;


public class SmsRequest {

    private String to;
    private String message;

    private String from;

    public SmsRequest(String payload, String from, String recipient) {
        this.message = payload;
        this.from = from;
        this.to = recipient;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
