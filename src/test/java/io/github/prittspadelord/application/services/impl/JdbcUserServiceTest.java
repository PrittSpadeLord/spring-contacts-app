package io.github.prittspadelord.application.services.impl;

import io.github.prittspadelord.application.data.dao.UserDao;
import io.github.prittspadelord.application.rest.models.CheckUsernameExistsResponse;
import io.github.prittspadelord.application.rest.models.RegisterUserRequest;
import io.github.prittspadelord.application.rest.models.RegisterUserResponse;
import io.github.prittspadelord.application.services.UniqueIdGenerationService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.time.Instant;

class JdbcUserServiceTest {

    private final Argon2PasswordEncoder passwordEncoder = Mockito.mock(Argon2PasswordEncoder.class);
    private final UniqueIdGenerationService uniqueIdGenerationService = Mockito.mock(UniqueIdGenerationService.class);
    private final UserDao userDao = Mockito.mock(UserDao.class);

    private final JdbcUserService jdbcUserService = new JdbcUserService(this.passwordEncoder, this.uniqueIdGenerationService, userDao);

    @Test
    public void checkUsernameShouldReturnFalseIfDoesntExist() {
        String username = "freshUsername";

        Mockito.when(this.userDao.checkUsername(username)).thenReturn(false);

        CheckUsernameExistsResponse checkUsernameExistsResponse = this.jdbcUserService.checkUsername(username);

        Assertions.assertFalse(checkUsernameExistsResponse.isResponse());
    }

    @Test
    public void checkUsernameShouldReturnTrueIfExists() {
        String username = "existingUsername";

        Mockito.when(this.userDao.checkUsername(username)).thenReturn(true);

        CheckUsernameExistsResponse checkUsernameExistsResponse = this.jdbcUserService.checkUsername(username);

        Assertions.assertTrue(checkUsernameExistsResponse.isResponse());
    }

    @Test
    public void createUserShouldBeSuccessful() {

        String username = "testUser";
        String nickname = "Test Nickname";
        String rawPassword = "rawPassword123";
        String hashedPassword = "$argon2id$v=19$m=16384,t=2,p=1$testingsaltvalue012345$thehashedvalueofthesalt/J2xEkVFYZ+HLWJkJ8XS";
        long id = 1577836800000L;

        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername(username);
        registerUserRequest.setNickname(nickname);
        registerUserRequest.setPassword(rawPassword);

        Mockito.when(this.passwordEncoder.encode(rawPassword))
            .thenReturn(hashedPassword);

        Mockito.when(this.uniqueIdGenerationService.generateUniqueId(Mockito.any(Instant.class)))
            .thenReturn(id);

        RegisterUserResponse registerUserResponse = this.jdbcUserService.createUser(registerUserRequest);

        Assertions.assertEquals(String.valueOf(id), registerUserResponse.getId(), "User id must be the same as what was generated");
        Assertions.assertEquals(username, registerUserResponse.getUsername(), "Username must be the same as what was supplied");
        Assertions.assertEquals(nickname, registerUserResponse.getNickname(), "Nickname must be the same as what was supplied");
    }
}