package io.github.prittspadelord.application.rest.interceptors;

import io.github.prittspadelord.application.rest.annotations.Authorized;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if(!handlerMethod.hasMethodAnnotation(Authorized.class)) {
            return true;
        }

        String jwtString = request.getHeader("Authorization");

        return false;
    }
}
