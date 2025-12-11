package io.github.prittspadelord.application.data.models;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private long id;
    @NotNull private String username;
    @NotNull private String nickname;
    @NotNull private String hashedPassword;
    private long recentPasswordUpdateTimestamp;
}