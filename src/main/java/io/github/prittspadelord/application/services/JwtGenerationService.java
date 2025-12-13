package io.github.prittspadelord.application.services;

import org.springframework.security.oauth2.jwt.Jwt;

public interface JwtGenerationService {
    Jwt generateJwt(long userId, String username); //ascertain all the params needed first
}
