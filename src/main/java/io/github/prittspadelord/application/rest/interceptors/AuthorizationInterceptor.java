package io.github.prittspadelord.application.rest.interceptors;

import io.github.prittspadelord.application.rest.annotations.Authorized;
import io.github.prittspadelord.application.rest.annotations.support.AuthorizationLevel;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if(!handlerMethod.hasMethodAnnotation(Authorized.class)) return true;

        AuthorizationLevel authorizationLevel = Objects.requireNonNull(handlerMethod.getMethodAnnotation(Authorized.class)).value();

        return switch(authorizationLevel) {
            case NONE -> true;
            case USER -> {
                //scan JWT token for user permissions

                String jwtString = request.getHeader("Authorization");

                yield false;
            }
            case ADMIN -> {
                //scan JWT token for admin permissions

                String jwtString = request.getHeader("Authorization");

                yield false;
            }
        };
    }
}
