package io.github.prittspadelord.application.rest.annotations;

import io.github.prittspadelord.application.rest.support.PasswordValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidPassword {
    String message() default "Must only contain characters on the QWERTY English keyboard";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}