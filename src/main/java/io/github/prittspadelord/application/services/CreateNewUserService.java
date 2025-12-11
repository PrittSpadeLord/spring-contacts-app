package io.github.prittspadelord.application.services;

import io.github.prittspadelord.application.rest.models.RegisterUserRequest;
import io.github.prittspadelord.application.rest.models.RegisterUserResponse;

public interface CreateNewUserService {
    RegisterUserResponse createUser(RegisterUserRequest registerUserRequest);
}
