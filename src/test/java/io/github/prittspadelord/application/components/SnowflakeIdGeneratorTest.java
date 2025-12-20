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
        long timestamp = (snowflake >> 19) + 1577836800000L;

        Assertions.assertEquals(now.toEpochMilli(), timestamp);
    }

    @Test
    public void snowflakeMachineIdIsCorrect() {
        Instant now = Instant.now();
        long snowflake = this.snowflakeIdGenerator.generateSnowflakeId(now);
        long machineId = (snowflake >> 11) & 0xFFL;

        Assertions.assertEquals(Long.parseLong(System.getenv("MACHINE_ID")), machineId);
    }

    @Test
    public void snowflakeWorkerIdIsCorrect() {
        Instant now = Instant.now();
        long snowflake = this.snowflakeIdGenerator.generateSnowflakeId(now);
        long workerId = (snowflake >> 3) & 0xFFL;

        Assertions.assertEquals(0L, workerId);
    }

    @Test
    public void snowflakeIncrementerIsCorrect() {
        Instant now = Instant.now();
        long snowflake = this.snowflakeIdGenerator.generateSnowflakeId(now);
        long incrementer = (snowflake) & 0x7L;

        Assertions.assertEquals(0L, incrementer);
    }
}