package io.github.prittspadelord.application.services;

import io.github.prittspadelord.application.components.SecureArgon2PasswordEncoder;
import io.github.prittspadelord.application.components.SnowflakeIdGenerator;
import io.github.prittspadelord.application.data.dao.UserDao;
import io.github.prittspadelord.application.rest.models.CheckUsernameExistsResponse;
import io.github.prittspadelord.application.rest.models.RegisterUserRequest;
import io.github.prittspadelord.application.rest.models.RegisterUserResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.time.Instant;
import java.util.Arrays;

class UserServiceTest {

    private final JwtEncoder jwtEncoder = Mockito.mock(JwtEncoder.class);
    private final SecureArgon2PasswordEncoder passwordEncoder = Mockito.mock(SecureArgon2PasswordEncoder.class);
    private final SnowflakeIdGenerator snowflakeIdGenerator = Mockito.mock(SnowflakeIdGenerator.class);
    private final UserDao userDao = Mockito.mock(UserDao.class);

    private final UserService userService = new UserService(this.jwtEncoder, this.passwordEncoder, this.snowflakeIdGenerator, userDao);

    @Test
    public void checkUsernameShouldReturnFalseIfDoesntExist() {
        String username = "freshUsername";

        Mockito.when(this.userDao.checkUsername(username)).thenReturn(false);

        CheckUsernameExistsResponse checkUsernameExistsResponse = this.userService.checkUsername(username);

        Assertions.assertFalse(checkUsernameExistsResponse.isDoesUsernameExist());
    }

    @Test
    public void checkUsernameShouldReturnTrueIfExists() {
        String username = "existingUsername";

        Mockito.when(this.userDao.checkUsername(username)).thenReturn(true);

        CheckUsernameExistsResponse checkUsernameExistsResponse = this.userService.checkUsername(username);

        Assertions.assertTrue(checkUsernameExistsResponse.isDoesUsernameExist());
    }

    @Test
    public void createUserShouldBeSuccessful() {

        String username = "testUser";
        String nickname = "Test Nickname";
        char[] rawPassword = {'r', 'a', 'w', 'P', 'a', 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3'};
        String hashedPassword = "$argon2id$v=19$m=16384,t=2,p=1$testingsaltvalue012345$thehashedvalueofthesalt/J2xEkVFYZ+HLWJkJ8XS";
        long id = 1577836800000L;

        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername(username);
        registerUserRequest.setNickname(nickname);
        registerUserRequest.setPassword(rawPassword);

        Mockito.when(this.passwordEncoder.encode(rawPassword))
            .thenReturn(hashedPassword);

        Arrays.fill(rawPassword, '\0'); //can this be incorporated into Mockito `when` logic?

        Mockito.when(this.snowflakeIdGenerator.generateSnowflakeId(Mockito.any(Instant.class)))
            .thenReturn(id);

        RegisterUserResponse registerUserResponse = this.userService.createUser(registerUserRequest);

        Assertions.assertEquals(String.valueOf(id), registerUserResponse.getId());
        Assertions.assertEquals(username, registerUserResponse.getUsername());
        Assertions.assertEquals(nickname, registerUserResponse.getNickname());
    }
}