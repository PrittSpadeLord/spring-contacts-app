package io.github.prittspadelord.application.rest.models;

import lombok.Getter;
import lombok.Setter;

import org.jspecify.annotations.NonNull;

@Getter
@Setter
public class ApiErrorInfo {
    private int status;
    @NonNull private String timestamp;
    @NonNull private String errorType;
    @NonNull private String description;
}
