package io.github.prittspadelord.application.rest.models;

import io.github.prittspadelord.application.data.models.Contact;
import org.jspecify.annotations.NonNull;

import java.time.Instant;

public class DeleteContactResponse {
    @NonNull private Instant timestamp;
    @NonNull private Contact contact;
}