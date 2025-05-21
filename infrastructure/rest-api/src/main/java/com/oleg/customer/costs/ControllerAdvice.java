package com.oleg.customer.costs;

import com.oleg.customer.costs.exception.NotFoundException;
import com.oleg.customer.costs.model.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return new ErrorDto();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleBadRequestException(IllegalArgumentException e) {
        logger.info("Bad request: {}", e.getMessage());
        return new ErrorDto().message(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleNotFoundException(NotFoundException e) {
        logger.info("Bad request: {}", e.getMessage());
        return new ErrorDto().message(e.getMessage());
    }
}