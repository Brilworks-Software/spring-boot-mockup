package com.brilworks.mockup.dto;

import java.time.LocalDateTime;

public class ErrorDto {
    private Integer status;

    private String errorMessage;

    private LocalDateTime timeStamp;

    public ErrorDto(Integer status, String errorMessage, LocalDateTime timeStamp) {
        this.status = status;
        this.errorMessage = errorMessage;
        this.timeStamp = timeStamp;
    }

    public Integer getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
