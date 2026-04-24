package io.github.prittspadelord.application.rest.models;

import lombok.Getter;
import lombok.Setter;

import org.jspecify.annotations.NonNull;

import java.time.Instant;

@Getter
@Setter
public class CreateContactResponse {
    private String id;
    @NonNull private Instant timestamp;
}