package io.github.prittspadelord.application.services.impl;

import io.github.prittspadelord.application.data.dao.UserDao;
import io.github.prittspadelord.application.rest.models.RegisterUserRequest;
import io.github.prittspadelord.application.rest.models.RegisterUserResponse;
import io.github.prittspadelord.application.services.UniqueIdGenerationService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.time.Instant;

class JdbcCreateNewUserServiceTest {

    private static final String USERNAME = "testUser";
    private static final String NICKNAME = "Test Nickname";
    private static final String RAW_PASSWORD = "rawPassword123";
    private static final String HASHED_PASSWORD = "$argon2id$v=19$m=16384,t=2,p=1$testingsaltvalue012345$thehashedvalueofthesalt/J2xEkVFYZ+HLWJkJ8XS";
    private static final long UNIQUE_ID = 1577836800000L;

    private final Argon2PasswordEncoder passwordEncoder = Mockito.mock(Argon2PasswordEncoder.class);
    private final UniqueIdGenerationService uniqueIdGenerationService = Mockito.mock(UniqueIdGenerationService.class);
    private final UserDao userDao = Mockito.mock(UserDao.class);

    private final JdbcCreateNewUserService jdbcCreateNewUserService = new JdbcCreateNewUserService(this.passwordEncoder, this.uniqueIdGenerationService, userDao);

    @Test
    public void createUserShouldBeSuccessful() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername(USERNAME);
        registerUserRequest.setNickname(NICKNAME);
        registerUserRequest.setPassword(RAW_PASSWORD);

        Mockito.when(this.passwordEncoder.encode(RAW_PASSWORD))
            .thenReturn(HASHED_PASSWORD);

        Mockito.when(this.uniqueIdGenerationService.generateUniqueId(Mockito.any(Instant.class)))
            .thenReturn(UNIQUE_ID);

        RegisterUserResponse registerUserResponse = this.jdbcCreateNewUserService.createUser(registerUserRequest);

        Assertions.assertEquals(String.valueOf(UNIQUE_ID), registerUserResponse.getId(), "User id must be the same as what was generated");
        Assertions.assertEquals(USERNAME, registerUserResponse.getUsername(), "Username must be the same as what was supplied");
        Assertions.assertEquals(NICKNAME, registerUserResponse.getNickname(), "Nickname must be the same as what was supplied");
    }
}