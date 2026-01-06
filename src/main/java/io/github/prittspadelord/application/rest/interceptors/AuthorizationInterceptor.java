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

    //private final ContactService contactService; //eliminate this
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
        long userId = Long.parseLong(jwt.getSubject());

        User user = this.userService.getUserFromId(userId); //this is still a database call...
        // is it safe to include the role within the JWT itself?
        // after all, the JWTs are generated internally, and it's impossible to sign a fraudulent JWT without knowing the server signature
        //moreover, we don't need the entire data. In this auth filter, we only use the user's pst as well as their role.

        if(!jwt.getClaim("pst").equals(String.valueOf(user.getRecentPasswordUpdateTimestamp()))) {
            // uh oh, we absolutely must make a database call here, there is no way around it
            // A really fast caching layer would help a ton for this.
            // PostgreSQL unlogged table is a good choice. If that's not fast enough, then Redis will do.
            throw new JwtRevokedException("JWT with pst of", jwt.getClaim("pst"), "can no longer be used to access resource for user who's password was reset on timestamp ", user.getRecentPasswordUpdateTimestamp());
        }

        if(user.getAuthorizationLevel() == AuthorizationLevel.ADMIN) return true;

        if(user.getAuthorizationLevel() != authorizationLevel) throw new InsufficientAuthorizationException("User with id", user.getId(), "with authorization level", user.getAuthorizationLevel().name(), "was prevented from accessing endpoint reserved for" + authorizationLevel.name());

        request.setAttribute("user_id", userId);

//        if(handlerMethod.getMethod().getName().contains("Contact")) {
//            long contactId = Long.parseLong(request.getParameter("id"));
//
//            long contactUserId = this.contactService.getUserId(contactId);
//
//            if(user.getId() != contactUserId) throw new UnauthorizedResourceAccessException("User with id", user.getId(), "is forbidden from accessing the contact with id", contactId, "that belongs to user of id", contactUserId);
//        }

        return true;
    }
}