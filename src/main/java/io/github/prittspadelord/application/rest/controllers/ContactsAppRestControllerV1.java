package io.github.prittspadelord.application.rest.controllers;

import io.github.prittspadelord.application.rest.models.RegisterUserRequest;
import io.github.prittspadelord.application.rest.models.RegisterUserResponse;
import io.github.prittspadelord.application.services.CreateNewUserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
@Slf4j
public class ContactsAppRestControllerV1 {

    private final CreateNewUserService createNewUserService;

    @PostMapping("/register")
    public RegisterUserResponse handleAccountRegistration(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        return this.createNewUserService.createUser(registerUserRequest);
    }
}