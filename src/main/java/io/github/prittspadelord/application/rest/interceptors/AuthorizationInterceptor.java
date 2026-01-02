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

    private final ContactService contactService;
    private final UserService userService;

    private final JwtDecoder jwtDecoder;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true; 
        }

        if(!handlerMethod.hasMethodAnnotation(Authorized.class)) return true;

        AuthorizationLevel authorizationLevel = Objects.requireNonNull(handlerMethod.getMethodAnnotation(Authorized.class)).value();

        Jwt jwt = this.jwtDecoder.decode(request.getHeader("Authorization"));

        User user = this.userService.getUserFromId(Long.parseLong(jwt.getSubject()));

        if(!jwt.getClaim("pst").equals(String.valueOf(user.getRecentPasswordUpdateTimestamp()))) {
            throw new JwtRevokedException("JWT with pst of", jwt.getClaim("pst"), "can no longer be used to access resource for user who's password was reset on timestamp ", user.getRecentPasswordUpdateTimestamp());
        }

        if(user.getAuthorizationLevel() == AuthorizationLevel.ADMIN) return true;

        if(user.getAuthorizationLevel() != authorizationLevel) throw new InsufficientAuthorizationException("User with id", user.getId(), "with authorization level", user.getAuthorizationLevel().name(), "was prevented from accessing endpoint reserved for" + authorizationLevel.name());

        if(handlerMethod.getMethod().getName().contains("Contact")) {
            long contactId = Long.parseLong(request.getParameter("id"));

            long contactUserId = this.contactService.getUserId(contactId);

            if(user.getId() != contactUserId) throw new UnauthorizedResourceAccessException("User with id", user.getId(), "is forbidden from accessing the contact with id", contactId, "that belongs to user of id", contactUserId);
        }

        return true;
    }
}