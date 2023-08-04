package com.brilworks.mockup.global;

import com.brilworks.mockup.dto.RestError;
import com.brilworks.mockup.dto.RestResponse;
import com.brilworks.mockup.exceptions.EntityNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionAdviceHandler {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ExceptionAdviceHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestResponse genericExceptionHandler(EntityNotFoundException e) {
        logger.error("Exception: ", e);
        e.printStackTrace();
        RestResponse res = new RestResponse(false);
        res.setError(new RestError(404, e.getMessage()));
        return res;
    }

}
