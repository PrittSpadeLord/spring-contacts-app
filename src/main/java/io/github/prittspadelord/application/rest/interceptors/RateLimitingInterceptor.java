package io.github.prittspadelord.application.rest.interceptors;

import io.github.prittspadelord.application.rest.RateLimitException;
import io.github.prittspadelord.application.components.BucketFactory;

import io.github.bucket4j.Bucket;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jspecify.annotations.NonNull;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/* NOTE! GET RID OF THIS ENTIRELY AFTER IMPLEMETING THIS WITHIN NGINX LAYER OF THE BROWSER APP */

// but! can we safely do this, since CORS is not safe enough

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitingInterceptor implements HandlerInterceptor {

    private final BucketFactory bucketFactory;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse ignoredResponse, @NonNull Object ignoredHandler) {

        Bucket bucket = bucketFactory.getBucket(request.getRemoteAddr());

        boolean wasTokenConsumed = bucket.tryConsume(1);
        bucketFactory.updateLastUsedTimestamp(request.getRemoteAddr());

        if(!wasTokenConsumed) {
            log.info("Bucket does not possess enough tokens, request from {} has been rate limited", request.getRemoteAddr());
            throw new RateLimitException();
        }

        log.info("Consumed 1 token from the bucket, request allowed. Remaining tokens in the bucket: {}", bucket.getAvailableTokens());
        return true;
    }
}