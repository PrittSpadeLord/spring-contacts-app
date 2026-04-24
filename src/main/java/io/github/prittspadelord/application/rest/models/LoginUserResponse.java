package io.github.prittspadelord.application.rest.models;

import lombok.Getter;
import lombok.Setter;

import org.jspecify.annotations.NonNull;

@Getter
@Setter
public class LoginUserResponse {
    @NonNull private String timestamp;
    @NonNull private String token;
}