package io.github.prittspadelord.application.rest.controllers;

import io.github.prittspadelord.application.rest.annotations.Authorized;
import io.github.prittspadelord.application.rest.models.CreateContactRequest;
import io.github.prittspadelord.application.services.ContactService;
import io.github.prittspadelord.application.support.AuthorizationLevel;
import io.github.prittspadelord.application.rest.models.CheckUsernameExistsResponse;
import io.github.prittspadelord.application.rest.models.CreateContactResponse;
import io.github.prittspadelord.application.rest.models.LoginUserRequest;
import io.github.prittspadelord.application.rest.models.LoginUserResponse;
import io.github.prittspadelord.application.rest.models.RegisterUserRequest;
import io.github.prittspadelord.application.rest.models.RegisterUserResponse;
import io.github.prittspadelord.application.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
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

    private final ContactService contactService;
    private final UserService userService;

    // Registering and Logging in

    @GetMapping("/checkUsername")
    public CheckUsernameExistsResponse handleUsernameCheck(@RequestParam("username") String username) {
        return this.userService.checkUsername(username);
    }

    @PostMapping("/login")
    public LoginUserResponse handleLogin(@Valid @RequestBody LoginUserRequest loginUserRequest) {
        return this.userService.loginUser(loginUserRequest);
    }

    @PostMapping("/register")
    public RegisterUserResponse handleAccountRegistration(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        return this.userService.createUser(registerUserRequest);
    }

    // Contacts CRUD

    @Authorized(AuthorizationLevel.USER)
    @PostMapping("/createContact")
    public CreateContactResponse handleContactCreation(@Valid @RequestBody CreateContactRequest createContactRequest, HttpServletRequest request) {
        return this.contactService.createContact(createContactRequest, request);
    }

    // Misc

    @Authorized(AuthorizationLevel.ADMIN)
    @GetMapping("/temp")
    public String temp() {
        return "temporary!";
    }
}