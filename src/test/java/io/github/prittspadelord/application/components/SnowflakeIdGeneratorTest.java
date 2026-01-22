package io.github.prittspadelord.application.components;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class SnowflakeIdGeneratorTest {

    private final SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator();

    @Test
    public void snowflakeTimestampIsCorrect() {
        Instant now = Instant.now();
        long snowflake = this.snowflakeIdGenerator.generateSnowflakeId(now);
        long timestamp = (snowflake >> 19) + SnowflakeIdGenerator.EPOCH_2020;

        Assertions.assertEquals(now.toEpochMilli(), timestamp);
    }

    @Test
    public void snowflakeRegionIdIsCorrect() {
        Instant now = Instant.now();
        long snowflake = this.snowflakeIdGenerator.generateSnowflakeId(now);
        long regionId = (snowflake >> 11) & 0xFFL;

        Assertions.assertEquals(Long.parseLong(System.getenv("REGION_ID")), regionId);
    }

    @Test
    public void snowflakeInstanceIdIsCorrect() {
        Instant now = Instant.now();
        long snowflake = this.snowflakeIdGenerator.generateSnowflakeId(now);
        long instanceId = (snowflake >> 3) & 0xFFL;

        Assertions.assertEquals(Long.parseLong(System.getenv("INSTANCE_ID")), instanceId);
    }

    @Test
    public void snowflakeIncrementerIsCorrect() {
        Instant now = Instant.now();
        long snowflake = this.snowflakeIdGenerator.generateSnowflakeId(now);
        long incrementer = (snowflake) & 0x7L;

        Assertions.assertEquals(0L, incrementer);
    }
}