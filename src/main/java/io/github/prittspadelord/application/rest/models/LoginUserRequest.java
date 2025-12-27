package io.github.prittspadelord.application.rest.models;

import io.github.prittspadelord.application.rest.annotations.ValidPassword;
import io.github.prittspadelord.application.rest.support.PasswordDeserializer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

import tools.jackson.databind.annotation.JsonDeserialize;

@Getter
@Setter
public class LoginUserRequest {

    @NotNull
    @Pattern(regexp = "[0-9a-z_]+")
    private String username;

    @JsonDeserialize(using = PasswordDeserializer.class)
    @NotNull
    @ValidPassword
    private char[] password;
}