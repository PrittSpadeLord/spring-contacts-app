package io.github.prittspadelord.application.services.support;

public class ResourceAccessException extends RuntimeException {
    public ResourceAccessException(String message) {
        super(message);
    }
}

//This should be deleted in favor of UnauthorizedException handling it