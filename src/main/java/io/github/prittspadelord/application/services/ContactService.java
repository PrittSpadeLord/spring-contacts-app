package io.github.prittspadelord.application.services;

import io.github.prittspadelord.application.data.dao.ContactDao;
import io.github.prittspadelord.application.rest.models.CreateContactRequest;
import io.github.prittspadelord.application.rest.models.CreateContactResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContactService {
    private final ContactDao contactDao;

    public CreateContactResponse createContact(CreateContactRequest createContactRequest) {
        //wip
        return null;
    }
}