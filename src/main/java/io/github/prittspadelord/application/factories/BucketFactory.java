package io.github.prittspadelord.application.factories;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.github.prittspadelord.application.wrappers.BucketWrapper;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class BucketFactory {

    ConcurrentHashMap<String, BucketWrapper> buckets = new ConcurrentHashMap<>();

    public Bucket getBucket(String remoteAddr) {

        BucketWrapper bucketWrapper = buckets.computeIfAbsent(remoteAddr, (_) -> {
            log.info("Bucket not found for remote address {}, commencing creation", remoteAddr);
            Bandwidth bandwidth = Bandwidth.builder()
                    .capacity(5)
                    .refillGreedy(1, Duration.ofSeconds(2))
                    .build();

            Bucket bucket = Bucket.builder()
                    .addLimit(bandwidth)
                    .build();

            log.info("Bucket successfully created for remote address {} and added to the map", remoteAddr);
            return new BucketWrapper(Instant.now().toEpochMilli(), bucket);
        });

        log.info("Bucket retrieved for remote address {}", remoteAddr);
        return bucketWrapper.getBucket();
    }

    public void updateLastUsedTimestamp(String remoteAddr) {
        BucketWrapper bucketWrapper = buckets.get(remoteAddr);
        bucketWrapper.setLastUsedTimestamp(Instant.now().toEpochMilli());
    }

    public boolean removeBuckets(Set<String> remoteAddrs) {
        log.info("Removing buckets for addresses {}", remoteAddrs.toString());
        return buckets.keySet().removeAll(remoteAddrs);
    }
}