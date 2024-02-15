package com.brilworks.mockup.exceptions;

import com.brilworks.mockup.dto.ErrorDto;
import com.brilworks.mockup.modules.social.dto.ErrorResponceDto;
import com.brilworks.mockup.modules.social.dto.ExceptionDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailNotSentException.class)
    @ResponseBody
    public ErrorDto tokenNotValidExceptionClass(EmailNotSentException e) {
        return new ErrorDto(e.getStatus(), e.getErrorMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ErrorResponceDto webApplicationException(BaseException baseException, HttpServletResponse response) {
        return new ErrorResponceDto(baseException.getErrorMessage(), baseException.getErrorCode(),
                baseException.getDeveloperMessage());
    }

    @ExceptionHandler(TokenNotValidException.class)
    @ResponseBody
    public ExceptionDto tokenNotValidExceptionClass(TokenNotValidException tokenNotValidException){
        return new ExceptionDto(tokenNotValidException.getStatus(),tokenNotValidException.getErrorMessage(),tokenNotValidException.getDeveloperMessage());
    }

}
