package io.github.prittspadelord.application.rest.controllers.v1;

import io.github.prittspadelord.application.rest.models.CheckUsernameExistsResponse;
import io.github.prittspadelord.application.rest.models.LoginUserRequest;
import io.github.prittspadelord.application.rest.models.LoginUserResponse;
import io.github.prittspadelord.application.rest.models.RegisterUserRequest;
import io.github.prittspadelord.application.rest.models.RegisterUserResponse;
import io.github.prittspadelord.application.services.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
@Slf4j
public class UserAuthRestController {

    private final UserService userService;

    @GetMapping("/checkUsername")
    public CheckUsernameExistsResponse handleUsernameCheck(@RequestParam("username") String username) {
        return this.userService.checkUsername(username);
    }

    @PostMapping("/login")
    public LoginUserResponse handleLogin(@Valid @RequestBody LoginUserRequest loginUserRequest) {
        return this.userService.loginUser(loginUserRequest);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterUserResponse handleAccountRegistration(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        return this.userService.createUser(registerUserRequest);
    }
}