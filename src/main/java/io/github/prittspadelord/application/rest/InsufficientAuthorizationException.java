package io.github.prittspadelord.application.rest;

import io.github.prittspadelord.application.support.VarargStringMessageRuntimeException;

public class InsufficientAuthorizationException extends VarargStringMessageRuntimeException {
    public InsufficientAuthorizationException(Object... messages) {
        super(messages);
    }
}