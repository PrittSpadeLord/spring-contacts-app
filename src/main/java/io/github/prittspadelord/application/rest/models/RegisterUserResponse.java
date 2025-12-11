package io.github.prittspadelord.application.rest.models;

import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;

import java.time.Instant;

@Getter
@Setter
public class RegisterUserResponse {
    private String id;
    @NonNull private String nickname;
    @NonNull private Instant timestamp;
    @NonNull private String username;
}