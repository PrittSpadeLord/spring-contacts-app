package io.github.prittspadelord.application.rest.annotations;

import io.github.prittspadelord.application.support.AuthorizationLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authorized {
    AuthorizationLevel value();
}