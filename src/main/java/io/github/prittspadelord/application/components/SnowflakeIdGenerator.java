package io.github.prittspadelord.application.components;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SnowflakeIdGenerator {

    public static final long EPOCH_2020 = 1577836800000L;

    private final long regionId;
    private final long instanceId;
    private long previousTimestamp;
    private long incrementer;

    public SnowflakeIdGenerator() {
        this.regionId = Long.parseLong(System.getenv("REGION_ID"));
        this.instanceId = Long.parseLong(System.getenv("INSTANCE_ID"));
        this.previousTimestamp = 0L;
        this.incrementer = 0L;
    }

    public synchronized long generateSnowflakeId() {

        long timestamp = Instant.now().toEpochMilli();

        if(timestamp < previousTimestamp) {
            long offset = previousTimestamp - timestamp;

            try {
                Thread.sleep(offset);
                timestamp = Instant.now().toEpochMilli();
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if(previousTimestamp != 0 && previousTimestamp == timestamp) incrementer++;
        else incrementer = 0L;

        previousTimestamp = timestamp;

        return ((timestamp - SnowflakeIdGenerator.EPOCH_2020) << 19) | (this.regionId << 11) | (this.instanceId << 3) | (this.incrementer);
    }
}