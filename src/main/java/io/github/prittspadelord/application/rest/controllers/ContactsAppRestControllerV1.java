package io.github.prittspadelord.application.rest.controllers;

import io.github.prittspadelord.application.rest.models.RegisterUserRequest;
import io.github.prittspadelord.application.rest.models.RegisterUserResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RequestMapping("/api/v1")
@RestController
@Slf4j
public class ContactsAppRestControllerV1 {

    @PostMapping("/register")
    public RegisterUserResponse handleAccountRegistration(@RequestBody RegisterUserRequest registerUserRequest) {

        log.info(registerUserRequest.getUsername());

        var registerAccountResponse = new RegisterUserResponse();
        registerAccountResponse.setId(String.valueOf(97882569828204544L));
        registerAccountResponse.setTimestamp(Instant.ofEpochMilli(1764532985738L));
        registerAccountResponse.setUsername("pritt0780");
        registerAccountResponse.setNickname("Nara Meztli Yanfei Simp");

        return registerAccountResponse;
    }
}

