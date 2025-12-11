package io.github.prittspadelord.application.rest.support;


import lombok.Getter;

import org.jspecify.annotations.Nullable;
import org.springframework.validation.FieldError;

@Getter
public class ValidationErrorEnumeration {
    private final String field;
    private final String message;

    public ValidationErrorEnumeration(FieldError fieldError) {
        this.field = fieldError.getField();
        this.message = this.transformDefaultMessages(fieldError.getDefaultMessage());
    }

    private String transformDefaultMessages(@Nullable String defaultMessage) {

        if("must not be null".equals(defaultMessage)) {
            return "Must not be empty";
        }

        if("must match \"[0-9a-z_]+\"".equals(defaultMessage)) {
            return "Must only contain lowercase alphanumeric and underscores";
        }

        if("must match \"[\\x20-\\x7E]+\"".equals(defaultMessage)) {
            return "Must only contain characters on the QWERTY English keyboard";
        }

        return "Unspecified error";
    }
}
