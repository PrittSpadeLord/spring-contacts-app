package io.github.prittspadelord.application.services;

import io.github.prittspadelord.application.components.SecureArgon2PasswordEncoder;
import io.github.prittspadelord.application.components.SnowflakeIdGenerator;
import io.github.prittspadelord.application.data.dao.UserDao;
import io.github.prittspadelord.application.data.models.User;
import io.github.prittspadelord.application.rest.models.CheckUsernameExistsResponse;
import io.github.prittspadelord.application.rest.models.RegisterUserRequest;
import io.github.prittspadelord.application.rest.models.RegisterUserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final SecureArgon2PasswordEncoder passwordEncoder;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final UserDao userDao;

    public CheckUsernameExistsResponse checkUsername(String string) {
        var checkUsernameExistsResponse = new CheckUsernameExistsResponse();
        checkUsernameExistsResponse.setResponse(this.userDao.checkUsername(string));

        return checkUsernameExistsResponse;
    }

    public RegisterUserResponse createUser(RegisterUserRequest registerUserRequest) {

        User user = new User();
        Instant now = Instant.now();
        long snowflakeId = this.snowflakeIdGenerator.generateSnowflakeId(now);
        String hashedPassword = this.passwordEncoder.encode(registerUserRequest.getPassword());

        user.setId(snowflakeId);
        user.setUsername(registerUserRequest.getUsername());
        user.setNickname(registerUserRequest.getNickname());
        user.setHashedPassword(hashedPassword);
        user.setRecentPasswordUpdateTimestamp(now.toEpochMilli());

        this.userDao.insertUser(user);
        log.info("Called the userDao to insert user with username {}", registerUserRequest.getUsername());

        var registerUserResponse = new RegisterUserResponse();
        registerUserResponse.setId(String.valueOf(snowflakeId));
        registerUserResponse.setTimestamp(now);
        registerUserResponse.setUsername(registerUserRequest.getUsername());
        registerUserResponse.setNickname(registerUserRequest.getNickname());

        return registerUserResponse;
    }
}