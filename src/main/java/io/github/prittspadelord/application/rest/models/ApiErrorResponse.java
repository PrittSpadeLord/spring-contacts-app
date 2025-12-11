package io.github.prittspadelord.application.rest.models;

import lombok.Getter;
import lombok.Setter;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

@Getter
@Setter
public class ApiErrorResponse {
    private int status;
    @NonNull private Instant timestamp;
    @NonNull private String errorType;
    @NonNull private String description;
    @Nullable private Object additionalData;
}