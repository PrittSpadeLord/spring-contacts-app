package io.github.prittspadelord.application.rest.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class RegisterUserResponse {
    private String id;
    private String nickname;
    private Instant timestamp;
    private String username;
}