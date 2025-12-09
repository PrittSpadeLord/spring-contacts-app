package io.github.prittspadelord.application.services.impl;

import io.github.prittspadelord.application.data.models.User;
import io.github.prittspadelord.application.rest.models.RegisterUserRequest;
import io.github.prittspadelord.application.services.CreateNewUserService;

import lombok.AllArgsConstructor;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@AllArgsConstructor
@Service
public class JdbcCreateNewUserService implements CreateNewUserService {

    private final Argon2PasswordEncoder passwordEncoder;

    @Override
    public User createUser(RegisterUserRequest registerUserRequest) {

        User user = new User();
        Instant now = Instant.now();

        user.setId(this.generateSnowflakeId(now));
        user.setUsername(registerUserRequest.getUsername());
        user.setNickname(registerUserRequest.getNickname());
        user.setHashedPassword(this.hashPassword(this.passwordEncoder, registerUserRequest.getPassword()));
        user.setRecentPasswordUpdateTimestamp(now.toEpochMilli());

        return user;
    }

    private long generateSnowflakeId(Instant instant) {
        long timestamp = instant.toEpochMilli();
        long machineId = Integer.parseInt(System.getenv("MACHINE_ID"));
        long threadId = Thread.currentThread().threadId();
        long incrementer = 0L; //for now, this will be thread-safe and atomically incremented for requests within the same millisecond

        return ((timestamp - 1577836800000L) << 19) + (machineId << 11) + (threadId << 3) + (incrementer);
    }

    private String hashPassword(Argon2PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.encode(password);
    }
}