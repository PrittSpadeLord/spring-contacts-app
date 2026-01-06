package io.github.prittspadelord.application.rest.interceptors;

import io.github.prittspadelord.application.data.models.User;
import io.github.prittspadelord.application.rest.InsufficientAuthorizationException;
import io.github.prittspadelord.application.rest.JwtRevokedException;
import io.github.prittspadelord.application.rest.UnauthorizedResourceAccessException;
import io.github.prittspadelord.application.rest.annotations.Authorized;
import io.github.prittspadelord.application.services.ContactService;
import io.github.prittspadelord.application.support.AuthorizationLevel;
import io.github.prittspadelord.application.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jspecify.annotations.NonNull;

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
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true; 
        }

        if(!handlerMethod.hasMethodAnnotation(Authorized.class)) return true;

        AuthorizationLevel requestAuthorizationLevel = Objects.requireNonNull(handlerMethod.getMethodAnnotation(Authorized.class)).value();

        Jwt jwt = this.jwtDecoder.decode(request.getHeader("Authorization"));
        long userId = Long.parseLong(jwt.getSubject());

        //User user = this.userService.getUserFromId(userId);
        long recentPasswordUpdateTimestamp = this.userService.getPSTForId(userId);

        if(!jwt.getClaim("pst").equals(String.valueOf(recentPasswordUpdateTimestamp))) {
            // uh oh, we absolutely must make a database call here, there is no way around it
            // A really fast caching layer would help a ton for this.
            // PostgreSQL unlogged table is a good choice. If that's not fast enough, then Redis will do.
            throw new JwtRevokedException("JWT with pst of", jwt.getClaim("pst"), "can no longer be used to access resource for user who's password was reset on timestamp ", recentPasswordUpdateTimestamp);
        }

        AuthorizationLevel userAuthorizationLevel = this.userService.getAuthorizationLevelForId(userId);

        if(userAuthorizationLevel == AuthorizationLevel.ADMIN) return true;

        if(userAuthorizationLevel != requestAuthorizationLevel) throw new InsufficientAuthorizationException("User with id", userId, "with authorization level", userAuthorizationLevel.name(), "was prevented from accessing endpoint reserved for" + requestAuthorizationLevel.name());

        request.setAttribute("user_id", userId);

        return true;
    }
}