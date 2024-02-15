package com.brilworks.mockup.modules.email.dto;

import jakarta.validation.constraints.NotNull;

public class EmailDto {
    @NotNull
    private String sender;
    @NotNull
    private String receiver;

    public EmailDto(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }
}
