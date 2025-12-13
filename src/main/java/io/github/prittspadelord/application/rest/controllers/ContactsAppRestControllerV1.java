package io.github.prittspadelord.application.rest.controllers;

import io.github.prittspadelord.application.rest.models.CheckUsernameExistsResponse;
import io.github.prittspadelord.application.rest.models.RegisterUserRequest;
import io.github.prittspadelord.application.rest.models.RegisterUserResponse;
import io.github.prittspadelord.application.services.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
@Slf4j
public class ContactsAppRestControllerV1 {
    private final UserService userService;

    @GetMapping("/checkUsername")
    public CheckUsernameExistsResponse handleUsernameCheck(@RequestParam("username") String username) {
        return this.userService.checkUsername(username);
    }

    @PostMapping("/register")
    public RegisterUserResponse handleAccountRegistration(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        return this.userService.createUser(registerUserRequest);
    }
}