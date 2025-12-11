package io.github.prittspadelord.application.services.impl;

import io.github.prittspadelord.application.data.dao.UserDao;
import io.github.prittspadelord.application.data.models.User;
import io.github.prittspadelord.application.rest.models.RegisterUserRequest;
import io.github.prittspadelord.application.rest.models.RegisterUserResponse;
import io.github.prittspadelord.application.services.CreateNewUserService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@AllArgsConstructor
@Service
@Slf4j
public class JdbcCreateNewUserService implements CreateNewUserService {

    private final Argon2PasswordEncoder passwordEncoder;
    private final UserDao userDao;

    @Override
    public RegisterUserResponse createUser(RegisterUserRequest registerUserRequest) {

        User user = new User();
        Instant now = Instant.now();
        long snowflakeId = this.generateSnowflakeId(now);
        String hashedPassword = this.hashPassword(this.passwordEncoder, registerUserRequest.getPassword());

        user.setId(snowflakeId);
        user.setUsername(registerUserRequest.getUsername());
        user.setNickname(registerUserRequest.getNickname());
        user.setHashedPassword(hashedPassword);
        user.setRecentPasswordUpdateTimestamp(now.toEpochMilli());

        userDao.insertUser(user);
        log.info("Called the userDao to insert user with username {}", registerUserRequest.getUsername());

        var registerUserResponse = new RegisterUserResponse();
        registerUserResponse.setId(String.valueOf(snowflakeId));
        registerUserResponse.setTimestamp(now);
        registerUserResponse.setUsername(registerUserRequest.getUsername());
        registerUserResponse.setNickname(registerUserRequest.getNickname());

        return registerUserResponse;
    }

    private long generateSnowflakeId(Instant instant) {
        long timestamp = instant.toEpochMilli();
        long machineId = Long.parseLong(System.getenv("MACHINE_ID"));
        long threadId = Thread.currentThread().threadId();
        long incrementer = 0L; //for now, later this will be thread-safe and atomically incremented for requests within the same millisecond

        return ((timestamp - 1577836800000L) << 19) + (machineId << 11) + (threadId << 3) + (incrementer);
    }

    private String hashPassword(Argon2PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.encode(password);
    }
}