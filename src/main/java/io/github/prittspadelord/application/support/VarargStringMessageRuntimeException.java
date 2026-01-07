package io.github.prittspadelord.application.support;

import java.util.StringJoiner;

public class VarargStringMessageRuntimeException extends RuntimeException {
    public VarargStringMessageRuntimeException(Object... args) {
        super(VarargStringMessageRuntimeException.join(args));
    }

    private static String join(Object[] args) {
        StringJoiner joiner = new StringJoiner(" ");
        for(Object arg: args) joiner.add(String.valueOf(arg));
        return joiner.toString();
    }
}