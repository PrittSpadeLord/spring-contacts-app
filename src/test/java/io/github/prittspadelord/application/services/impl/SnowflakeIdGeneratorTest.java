package io.github.prittspadelord.application.services.impl;

import io.github.prittspadelord.application.components.SnowflakeIdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class SnowflakeIdGeneratorTest {

    private final SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator();

    @Test
    public void snowflakeTimestampIsCorrect() {
        Instant now = Instant.now();
        long snowflake = this.snowflakeIdGenerator.generateUniqueId(now);
        long timestamp = (snowflake >> 19) + 1577836800000L;

        Assertions.assertEquals(now.toEpochMilli(), timestamp, "Timestamp component of snowflake matches the timestamp of snowflake generation");
    }

    @Test
    public void snowflakeMachineIdIsCorrect() {
        Instant now = Instant.now();
        long snowflake = this.snowflakeIdGenerator.generateUniqueId(now);
        long machineId = (snowflake >> 11) & 0xFFL;

        Assertions.assertEquals(Long.parseLong(System.getenv("MACHINE_ID")), machineId, "Machine id component of snowflake matches machine id from environmental variable");
    }

    @Test
    public void snowflakeThreadIdIsCorrect() {

        Instant now = Instant.now();
        long snowflake = this.snowflakeIdGenerator.generateUniqueId(now);
        long threadId = (snowflake >> 3) & 0xFFL;

        Assertions.assertEquals(Thread.currentThread().threadId(), threadId, "Thread id component of snowflake matches current thread id");
    }

    @Test
    public void snowflakeIncrementerIsCorrect() {
        Instant now = Instant.now();
        long snowflake = this.snowflakeIdGenerator.generateUniqueId(now);
        long incrementer = (snowflake) & 0x7L;

        Assertions.assertEquals(0L, incrementer, "Incrementer value is 0. This will be changed later");
    }
}