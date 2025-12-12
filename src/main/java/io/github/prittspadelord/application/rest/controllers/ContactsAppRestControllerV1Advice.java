package io.github.prittspadelord.application.rest.controllers;

import io.github.prittspadelord.application.rest.RateLimitException;
import io.github.prittspadelord.application.rest.models.ApiErrorResponse;
import io.github.prittspadelord.application.rest.support.ValidationErrorEnumeration;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ContactsAppRestControllerV1Advice {

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorResponse duplicateEntryExceptionHandler(DuplicateKeyException e, HttpServletRequest req) {
        var error = new ApiErrorResponse();
        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setTimestamp(Instant.now());
        error.setErrorType(HttpStatus.FORBIDDEN.name());
        error.setDescription("The username you provided already exists in the database. Why did you bypass the client-side validation huh?");
        error.setAdditionalData(null);

        log.info("Proper error message with status {} has been sent to user of remote address {} for triggering {} with message: {}", HttpStatus.FORBIDDEN.value(), req.getRemoteAddr(), e.getClass().getName(), e.getMessage());
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse validationExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest req) {
        var error = new ApiErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setTimestamp(Instant.now());
        error.setErrorType(HttpStatus.BAD_REQUEST.name());
        error.setDescription("The input you have provided is invalid! See additionalData for more information");
        error.setAdditionalData(null);

        List<ValidationErrorEnumeration> validationErrors = e.getFieldErrors().stream().map(ValidationErrorEnumeration::new).toList();

        error.setAdditionalData(validationErrors);

        log.info("Proper error message with status {} has been sent to user of remote address {} for triggering {} with message: {}", HttpStatus.BAD_REQUEST.value(), req.getRemoteAddr(), e.getClass().getName(), e.getMessage());
        return error;
    }

    @ExceptionHandler(RateLimitException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ApiErrorResponse rateLimitExceptionHander(RateLimitException e, HttpServletRequest req) {
        var error = new ApiErrorResponse();
        error.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        error.setTimestamp(Instant.now());
        error.setErrorType(HttpStatus.TOO_MANY_REQUESTS.name());
        error.setDescription("You are sending too many requests! Slow down and try again!");
        error.setAdditionalData(null);

        log.info("Proper error message with status {} has been sent to user of remote address {} for triggering {} with message: {}", HttpStatus.TOO_MANY_REQUESTS.value(), req.getRemoteAddr(), e.getClass().getName(), e.getMessage());
        return error;
    }


    //Catch-all handler placed at the very bottom
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse genericExceptionHandler(Exception e, HttpServletRequest req) {
        var error = new ApiErrorResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setTimestamp(Instant.now());
        error.setErrorType(HttpStatus.INTERNAL_SERVER_ERROR.name());
        error.setDescription("An unknown error has occured! Don't worry, we have recorded what just happened and will investigate shortly! We apologize for the inconvenience.");
        error.setAdditionalData(null);

        log.warn("Unclassified exception occured while processing request from {} of type {} with message: {}", req.getRemoteAddr(), e.getClass().getName(), e.getMessage());
        return error;
    }
}
