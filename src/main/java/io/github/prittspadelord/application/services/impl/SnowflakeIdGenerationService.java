package io.github.prittspadelord.application.services.impl;

import io.github.prittspadelord.application.services.UniqueIdGenerationService;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SnowflakeIdGenerationService implements UniqueIdGenerationService {
    
    @Override
    public long generateUniqueId(Instant instant) {
        long timestamp = instant.toEpochMilli();
        long machineId = Long.parseLong(System.getenv("MACHINE_ID"));
        long threadId = Thread.currentThread().threadId();
        long incrementer = 0L; //for now, later this will be thread-safe and atomically incremented for requests within the same millisecond

        return ((timestamp - 1577836800000L) << 19) + (machineId << 11) + (threadId << 3) + (incrementer);
    }
}