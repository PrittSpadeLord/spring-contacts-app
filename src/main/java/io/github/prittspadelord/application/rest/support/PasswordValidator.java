package io.github.prittspadelord.application.rest.support;

import io.github.prittspadelord.application.rest.annotations.ValidPassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.bouncycastle.util.Arrays;

import java.nio.CharBuffer;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, char[]> {

    private static final Pattern passwordPattern = Pattern.compile("[\\x20-\\x7E]+");

    @Override
    public boolean isValid(char[] value, ConstraintValidatorContext context) {
        CharBuffer buffer = CharBuffer.wrap(value);
        boolean doesMatch = PasswordValidator.passwordPattern.matcher(buffer).matches();

        if(!doesMatch) {
            Arrays.fill(value, '\0');
        }

        return doesMatch;
    }
}