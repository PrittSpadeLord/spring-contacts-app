package io.github.prittspadelord.application.services;

import io.github.prittspadelord.application.data.models.User;
import io.github.prittspadelord.application.rest.models.RegisterUserRequest;

public interface CreateNewUserService {
    User createUser(RegisterUserRequest registerUserRequest);
}
