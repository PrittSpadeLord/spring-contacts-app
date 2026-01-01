package io.github.prittspadelord.application.rest.models;

import io.github.prittspadelord.application.data.models.Contact;

import lombok.Getter;
import lombok.Setter;

import org.jspecify.annotations.NonNull;

import java.time.Instant;

@Getter
@Setter
public class GetContactResponse {
    @NonNull private Instant timestamp;
    @NonNull private Contact contact;
}
