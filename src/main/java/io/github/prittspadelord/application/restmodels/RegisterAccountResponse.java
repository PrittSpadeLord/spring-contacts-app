package io.github.prittspadelord.application.restmodels;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class RegisterAccountResponse {
    private String id;
    private String nickname;
    private Instant timestamp;
    private String username;
}