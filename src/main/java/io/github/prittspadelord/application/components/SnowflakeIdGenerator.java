package io.github.prittspadelord.application.components;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

/* WARNING: THIS CLASS IS UNDER DEVELOPMENT AND IS IN AN INCOMPLETE STATE. Tests on this class may fail */

@Component
public class SnowflakeIdGenerator {

    public static final long EPOCH_2020 = 1577836800000L;

    private final long regionId;
    private final long instanceId;
    private final AtomicLong previousTimestamp;
    private final AtomicLong incrementer;

    public SnowflakeIdGenerator() {
        this.regionId = Long.parseLong(System.getenv("REGION_ID"));
        this.instanceId = Long.parseLong(System.getenv("INSTANCE_ID"));
        this.previousTimestamp = new AtomicLong(0L);
        this.incrementer = new AtomicLong(0L);
    }

    public long generateSnowflakeId(Instant instant) {
        long timestamp = instant.toEpochMilli();
        handleIncrementer(previousTimestamp.get(), timestamp);
        previousTimestamp.set(timestamp);

        return ((timestamp - SnowflakeIdGenerator.EPOCH_2020) << 19) + (this.regionId << 11) + (this.instanceId << 3) + (this.incrementer.get());
    }

    private void handleIncrementer(long previousTimestamp, long timeStamp) {
        if(previousTimestamp != 0 && previousTimestamp == timeStamp) incrementer.incrementAndGet();
        else incrementer.set(0L);
    }
}