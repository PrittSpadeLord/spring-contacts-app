package io.github.prittspadelord.application.services;

import io.github.prittspadelord.application.rest.models.CheckUsernameExistsResponse;
import io.github.prittspadelord.application.rest.models.RegisterUserRequest;
import io.github.prittspadelord.application.rest.models.RegisterUserResponse;

public interface UserService {
    CheckUsernameExistsResponse checkUsername(String string);
    RegisterUserResponse createUser(RegisterUserRequest registerUserRequest);
}
