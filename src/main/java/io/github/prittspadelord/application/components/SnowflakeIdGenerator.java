package io.github.prittspadelord.application.components;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SnowflakeIdGenerator {

    public long generateSnowflakeId(Instant instant) {
        long timestamp = instant.toEpochMilli();
        long regionId = Long.parseLong(System.getenv("REGION_ID"));
        long instanceId = Long.parseLong(System.getenv("INSTANCE_ID"));
        long incrementer = 0L; //for now, later this will be thread-safe and atomically incremented for requests within the same millisecond

        return ((timestamp - 1577836800000L) << 19) + (regionId << 11) + (instanceId << 3) + (incrementer);
    }
}