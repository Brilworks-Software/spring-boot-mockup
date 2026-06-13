package com.brilworks.mockup.modules.email.dto;

public class EmailSuccessDto {

    private Integer status;

    private String message;

    public EmailSuccessDto(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
