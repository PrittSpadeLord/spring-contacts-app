package io.github.prittspadelord.application.services;

import java.time.Instant;

public interface UniqueIdGenerationService {
    long generateUniqueId(Instant instant);
}