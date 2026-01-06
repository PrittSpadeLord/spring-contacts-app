package io.github.prittspadelord.application.services;

import io.github.prittspadelord.application.components.SecureArgon2PasswordEncoder;
import io.github.prittspadelord.application.components.SnowflakeIdGenerator;
import io.github.prittspadelord.application.data.dao.UserDao;
import io.github.prittspadelord.application.data.models.User;
import io.github.prittspadelord.application.rest.models.CheckUsernameExistsResponse;
import io.github.prittspadelord.application.rest.models.LoginUserRequest;
import io.github.prittspadelord.application.rest.models.LoginUserResponse;
import io.github.prittspadelord.application.rest.models.RegisterUserRequest;
import io.github.prittspadelord.application.rest.models.RegisterUserResponse;
import io.github.prittspadelord.application.services.support.IncorrectPasswordException;
import io.github.prittspadelord.application.support.AuthorizationLevel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final JwtEncoder jwtEncoder;
    private final SecureArgon2PasswordEncoder passwordEncoder;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final UserDao userDao;

    public CheckUsernameExistsResponse checkUsername(String string) {
        var checkUsernameExistsResponse = new CheckUsernameExistsResponse();
        checkUsernameExistsResponse.setDoesUsernameExist(this.userDao.checkUsername(string));

        return checkUsernameExistsResponse;
    }

    public RegisterUserResponse createUser(RegisterUserRequest registerUserRequest) {

        User user = new User();
        Instant now = Instant.now();
        long snowflakeId = this.snowflakeIdGenerator.generateSnowflakeId(now);
        String hashedPassword = this.passwordEncoder.encode(registerUserRequest.getPassword());

        user.setId(snowflakeId);
        user.setAuthorizationLevel(AuthorizationLevel.USER);
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

    public AuthorizationLevel getAuthorizationLevelForId(long id) {
        return this.userDao.getAuthorizationLevelForId(id);
    }

    public long getPSTForId(long id) {
        return this.userDao.getPSTForId(id);
    }

    public LoginUserResponse loginUser(LoginUserRequest loginUserRequest) {

        User user = this.userDao.getUserFromUsername(loginUserRequest.getUsername());

        if(!this.passwordEncoder.matches(loginUserRequest.getPassword(), user.getHashedPassword())) {
            throw new IncorrectPasswordException();
        }

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256)
            .type("JWT")
            .build();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
            .audience(Collections.singletonList(System.getenv("BASE_URL")))
            .claim("pst", String.valueOf(user.getRecentPasswordUpdateTimestamp()))
            .issuedAt(Instant.now())
            .issuer(System.getenv("BASE_URL"))
            .subject(String.valueOf(user.getId()))
            .build();

        Jwt jwt = this.jwtEncoder.encode(JwtEncoderParameters.from(header, claimsSet));

        LoginUserResponse loginUserResponse = new LoginUserResponse();
        loginUserResponse.setTimestamp(String.valueOf(Instant.now().toEpochMilli()));
        loginUserResponse.setToken(jwt.getTokenValue());

        return loginUserResponse;
    }
}