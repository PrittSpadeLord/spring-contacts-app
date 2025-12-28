package io.github.prittspadelord.application.rest.interceptors;

import io.github.prittspadelord.application.data.models.User;
import io.github.prittspadelord.application.rest.annotations.Authorized;
import io.github.prittspadelord.application.rest.annotations.support.AuthorizationLevel;

import io.github.prittspadelord.application.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtDecoder jwtDecoder;
    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if(!handlerMethod.hasMethodAnnotation(Authorized.class)) return true;

        AuthorizationLevel authorizationLevel = Objects.requireNonNull(handlerMethod.getMethodAnnotation(Authorized.class)).value();

        return switch(authorizationLevel) {
            case NONE -> true;
            case ADMIN -> {
                User user = this.getUserFromJwtString(request.getHeader("Authorization"));

                //check if user object has admin

                yield false;
            }
            case USER -> {
                User user = this.getUserFromJwtString(request.getHeader("Authorization"));

                yield false;
            }
        };
    }

    private User getUserFromJwtString(String jwtString) {
        Jwt jwt = jwtDecoder.decode(jwtString);

        return userService.getUserFromId(Long.parseLong(jwt.getSubject()));
    }
}
