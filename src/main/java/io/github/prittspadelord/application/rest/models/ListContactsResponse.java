package io.github.prittspadelord.application.rest.models;

import io.github.prittspadelord.application.data.models.Contact;

import lombok.Getter;
import lombok.Setter;

import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ListContactsResponse {
    @NonNull private Instant timestamp;
    @NonNull private List<Contact> contacts;
}
