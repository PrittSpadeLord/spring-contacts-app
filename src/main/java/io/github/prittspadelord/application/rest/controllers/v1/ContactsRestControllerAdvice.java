package io.github.prittspadelord.application.rest.controllers.v1;

import io.github.prittspadelord.application.rest.models.ApiErrorResponse;
import io.github.prittspadelord.application.rest.UnauthorizedResourceAccessException;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Order(1) //edit this to be more semantic: 1 is meaningless
@RestControllerAdvice
@Slf4j
public class ContactsRestControllerAdvice {

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