package io.github.prittspadelord.application.rest.support;

import io.github.prittspadelord.application.rest.annotations.ValidPassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.nio.CharBuffer;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, char[]> {

    @Override
    public boolean isValid(char[] value, ConstraintValidatorContext context) {
        CharBuffer buffer = CharBuffer.wrap(value);

        return Pattern.compile("[\\x20-\\x7E]+").matcher(buffer).matches();
    }
}