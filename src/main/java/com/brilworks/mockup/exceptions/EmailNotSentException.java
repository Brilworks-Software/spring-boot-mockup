package com.brilworks.mockup.exceptions;

public class EmailNotSentException extends RuntimeException{
    private Integer status;

    private String errorMessage;

    public EmailNotSentException(Integer status, String errorMessage) {
        super(errorMessage);
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public Integer getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
