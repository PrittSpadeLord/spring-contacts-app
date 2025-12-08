package io.github.prittspadelord.application.rest.models;

import lombok.Getter;
import lombok.Setter;

import org.jspecify.annotations.NonNull;

@Getter
@Setter
public class RegisterUserRequest {
    @NonNull private String username;
    @NonNull private String nickname;
    @NonNull private String password;
}
