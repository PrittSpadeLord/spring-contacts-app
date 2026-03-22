package io.github.prittspadelord.application.components;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

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

    public synchronized long generateSnowflakeId(Instant instant) {

        long timestamp = instant.toEpochMilli();

        if(timestamp < previousTimestamp.get()) {
            long offset = previousTimestamp.get() - timestamp;

            try {
                Thread.sleep(offset);
                timestamp = Instant.now().toEpochMilli();
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if(previousTimestamp.get() != 0 && previousTimestamp.get() == timestamp) incrementer.incrementAndGet();
        else incrementer.set(0L);

        previousTimestamp.set(timestamp);

        return ((timestamp - SnowflakeIdGenerator.EPOCH_2020) << 19) | (this.regionId << 11) | (this.instanceId << 3) | (this.incrementer.get());
    }
}