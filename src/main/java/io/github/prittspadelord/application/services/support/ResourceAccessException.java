package io.github.prittspadelord.application.services.support;

public class ResourceAccessException extends RuntimeException {
    public ResourceAccessException(String message) {
        super(message);
    }
}