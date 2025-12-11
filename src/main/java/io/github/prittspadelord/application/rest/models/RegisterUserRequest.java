package io.github.prittspadelord.application.rest.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {
    @NotNull @Pattern(regexp = "[0-9a-z_]+") private String username;
    @NotNull @Pattern(regexp = "[\\x20-\\x7E]+") private String nickname;
    @NotNull @Pattern(regexp = "[\\x20-\\x7E]+") private String password;
}
