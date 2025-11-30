package io.github.prittspadelord.application.controllers;

import io.github.prittspadelord.application.restmodels.RegisterAccountResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RequestMapping("/api/v1")
@RestController
@Slf4j
public class ContactsAppRestControllerV1 {

    @GetMapping("/register")
    public RegisterAccountResponse handleAccountRegistration() {

        var registerAccountResponse = new RegisterAccountResponse();
        registerAccountResponse.setId(String.valueOf(97882569828204544L));
        registerAccountResponse.setTimestamp(Instant.ofEpochMilli(1764532985738L));
        registerAccountResponse.setUsername("pritt0780");
        registerAccountResponse.setNickname("Nara Meztli Yanfei Simp");

        return registerAccountResponse;
    }
}

