package io.github.prittspadelord.application.rest.models;

import io.github.prittspadelord.application.rest.annotations.ValidPassword;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserRequest {
    @NotNull @Pattern(regexp = "[0-9a-z_]+") private String username;
    @NotNull @ValidPassword private char[] password; //need to ensure Jackson doesn't deserialize into intermediate String anywhere
}