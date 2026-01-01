package io.github.prittspadelord.application.rest;

import io.github.prittspadelord.application.support.VarargStringMessageRuntimeException;

public class UnauthorizedResourceAccessException extends VarargStringMessageRuntimeException {
    public UnauthorizedResourceAccessException(Object... messages) {
        super(messages);
    }
}