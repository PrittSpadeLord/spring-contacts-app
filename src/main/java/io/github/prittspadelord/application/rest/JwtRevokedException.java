package io.github.prittspadelord.application.rest;

import io.github.prittspadelord.application.support.VarargStringMessageRuntimeException;

public class JwtRevokedException extends VarargStringMessageRuntimeException {
    public JwtRevokedException(Object... messages) {
        super(messages);
    }
}