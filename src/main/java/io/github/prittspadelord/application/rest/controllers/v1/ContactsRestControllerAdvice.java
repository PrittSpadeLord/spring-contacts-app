package io.github.prittspadelord.application.rest.controllers.v1;

import io.github.prittspadelord.application.rest.models.ApiErrorResponse;
import io.github.prittspadelord.application.rest.UnauthorizedResourceAccessException;
import io.github.prittspadelord.application.rest.models.MissingParameterAdditionalData;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class ContactsRestControllerAdvice {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleEmptyResultDataAccessException(EmptyResultDataAccessException e, HttpServletRequest req) {
        var error = new ApiErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setTimestamp(Instant.now());
        error.setErrorType(HttpStatus.NOT_FOUND.name());
        error.setDescription("The resource(s) you request don't exist!");
        error.setAdditionalData(null);

        log.info("Proper error message with status {} has been sent to user of remote address {} for triggering {} with message: {}", HttpStatus.NOT_FOUND.value(), req.getRemoteAddr(), e.getClass().getName(), e.getMessage());
        return error;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest req) {
        var error = new ApiErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setTimestamp(Instant.now());
        error.setErrorType(HttpStatus.BAD_REQUEST.name());
        error.setDescription("You are missing some important URL parameters! Check additionalData for more information");

        List<MissingParameterAdditionalData> missingParameters = new ArrayList<>();

        MissingParameterAdditionalData missingParameter = new MissingParameterAdditionalData();
        missingParameter.setParameterName(e.getParameterName());
        missingParameter.setParameterType(e.getParameterType());

        missingParameters.add(missingParameter); //this only adds for a single missing parameter, how can we make it do so for more?

        error.setAdditionalData(missingParameters);

        log.info("Proper error message with status {} has been sent to user of remote address {} for triggering {} with message: {}", HttpStatus.BAD_REQUEST.value(), req.getRemoteAddr(), e.getClass().getName(), e.getMessage());
        return error;
    }

    @ExceptionHandler(UnauthorizedResourceAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleUnauthorizedResourceAccessException(UnauthorizedResourceAccessException e, HttpServletRequest req) {

        var error = new ApiErrorResponse();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setTimestamp(Instant.now());
        error.setErrorType(HttpStatus.UNAUTHORIZED.name());
        error.setDescription("You do not have the authority to access this resource!");
        error.setAdditionalData(null);

        log.info("Proper error message with status {} has been sent to user of remote address {} for triggering {} with message: {}", HttpStatus.UNAUTHORIZED.value(), req.getRemoteAddr(), e.getClass().getName(), e.getMessage());
        return error;
    }
}