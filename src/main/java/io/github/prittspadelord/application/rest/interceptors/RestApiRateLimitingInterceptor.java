package io.github.prittspadelord.application.rest.interceptors;

import io.github.bucket4j.Bucket;
import io.github.prittspadelord.application.rest.RateLimitException;
import io.github.prittspadelord.application.factories.BucketFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class RestApiRateLimitingInterceptor implements HandlerInterceptor {

    private final BucketFactory bucketFactory;

    @Autowired
    public RestApiRateLimitingInterceptor(BucketFactory bucketFactory) {
        this.bucketFactory = bucketFactory;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse ignoredResponse, Object ignoredHandler) {

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