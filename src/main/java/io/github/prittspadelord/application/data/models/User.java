package io.github.prittspadelord.application.data.models;

import lombok.Getter;
import lombok.Setter;

import org.jspecify.annotations.NonNull;

@Getter
@Setter
public class User {
    private long id;
    @NonNull private String username;
    @NonNull private String nickname;
    @NonNull private String hashedPassword;
    private long recentPasswordUpdateTimestamp;
}