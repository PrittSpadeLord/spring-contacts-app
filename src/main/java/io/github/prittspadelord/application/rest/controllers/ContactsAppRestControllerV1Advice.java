package io.github.prittspadelord.application.rest.controllers;

import io.github.prittspadelord.application.rest.RateLimitException;
import io.github.prittspadelord.application.rest.models.ApiErrorInfo;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class ContactsAppRestControllerV1Advice {

    @ExceptionHandler(RateLimitException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ApiErrorInfo rateLimitExceptionHander(Exception e, HttpServletRequest req) {
        var error = new ApiErrorInfo();
        error.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        error.setTimestamp(Instant.now().toString());
        error.setErrorType(HttpStatus.TOO_MANY_REQUESTS.name());
        error.setDescription("You are sending too many requests! Slow down and try again!");
        log.info("Proper error message with status {} has been sent to user of remote address {} for triggering {} ", HttpStatus.TOO_MANY_REQUESTS.value(), req.getRemoteAddr(), e.getClass().getName());
        return error;
    }


    //Catch-all handler placed at the very bottom
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorInfo genericExceptionHandler(Exception e, HttpServletRequest req) {
        var error = new ApiErrorInfo();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setTimestamp(Instant.now().toString());
        error.setErrorType(HttpStatus.INTERNAL_SERVER_ERROR.name());
        error.setDescription("An unknown error has occured! Don't worry, we have recorded what just happened and will investigate shortly! We apologize for the inconvenience.");
        log.warn("Unclassified exception occured while processing request from {} of type {} with message: {}", req.getRemoteAddr(), e.getClass().getName(), e.getMessage());
        return error;
    }
}
