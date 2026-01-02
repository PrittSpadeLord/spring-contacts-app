package io.github.prittspadelord.application.rest.controllers.v1;

import io.github.prittspadelord.application.rest.JwtRevokedException;
import io.github.prittspadelord.application.rest.models.ApiErrorResponse;
import io.github.prittspadelord.application.services.support.IncorrectPasswordException;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Order(1) //edit this to be more semantic: 1 is meaningless
@RestControllerAdvice
@Slf4j
public class UserAuthRestControllerAdvice {

    @ExceptionHandler(BadJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleBadJwtException(BadJwtException e, HttpServletRequest req) {
        var error = new ApiErrorResponse();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setTimestamp(Instant.now());
        error.setErrorType(HttpStatus.UNAUTHORIZED.name());
        error.setDescription("The JWT provided is invalid!");
        error.setAdditionalData(null);

        log.info("Proper error message with status {} has been sent to user of remote address {} for triggering {} with message: {}", HttpStatus.UNAUTHORIZED.value(), req.getRemoteAddr(), e.getClass().getName(), e.getMessage());
        return error;
    }

    @ExceptionHandler(JwtRevokedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleJwtRevokedException(JwtRevokedException e, HttpServletRequest req) {
        var error = new ApiErrorResponse();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setTimestamp(Instant.now());
        error.setErrorType(HttpStatus.UNAUTHORIZED.name());
        error.setDescription("The JWT provided has been revoked and is no longer valid!");
        error.setAdditionalData(null);

        log.info("Proper error message with status {} has been sent to user of remote address {} for triggering {} with message: {}", HttpStatus.UNAUTHORIZED.value(), req.getRemoteAddr(), e.getClass().getName(), e.getMessage());
        return error;
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleIncorrectPasswordException(IncorrectPasswordException e, HttpServletRequest req) {

        var error = new ApiErrorResponse();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setTimestamp(Instant.now());
        error.setErrorType(HttpStatus.UNAUTHORIZED.name());
        error.setDescription("The password you have supplied is incorrect!");
        error.setAdditionalData(null);

        log.info("Proper error message with status {} has been sent to user of remote address {} for triggering {} with message: {}", HttpStatus.UNAUTHORIZED.value(), req.getRemoteAddr(), e.getClass().getName(), e.getMessage());
        return error;
    }
}