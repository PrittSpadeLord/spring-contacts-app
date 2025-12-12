package io.github.prittspadelord.application.services;

public interface UniqueIdGenerationService {
    long generateUniqueId(Instant instant);
}