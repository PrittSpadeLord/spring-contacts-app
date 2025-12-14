package io.github.prittspadelord.application.components;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SnowflakeIdGenerator {

    public long generateSnowflakeId(Instant instant) {
        long timestamp = instant.toEpochMilli();
        long machineId = Long.parseLong(System.getenv("MACHINE_ID"));
        long threadId = Thread.currentThread().threadId();
        long incrementer = 0L; //for now, later this will be thread-safe and atomically incremented for requests within the same millisecond

        return ((timestamp - 1577836800000L) << 19) + (machineId << 11) + (threadId << 3) + (incrementer);
    }
}