package io.github.prittspadelord.application.services;

import io.github.prittspadelord.application.data.dao.ContactDao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

class ContactServiceTest {

    private final ContactDao contactDao = Mockito.mock(ContactDao.class);

    private final ContactService contactService = new ContactService(contactDao);

    @Test
    public void dummy() {
        Assertions.assertTrue(true);
    }
}